package com.tokyo.train.repository;

import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.entity.DelayEventEntity;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** JDBC(PreparedStatement)で MySQL にアクセスする。 */
@Repository
public class DelayRepository {

    private final DataSource dataSource;

    public DelayRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** 路線名から train_lines の id を取得する。見つからなければ null。 */
    public Integer findLineIdByName(String lineName) {
        if (lineName == null) return null;
        String sql = "SELECT id FROM train_lines WHERE line_name = ? LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lineName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("路線 ID の取得に失敗しました", e);
        }
    }

    /** 遅延イベントを delay_events に保存する。 */
    public void saveEvent(DelayEventEntity event) {
        String sql = "INSERT INTO delay_events (line_id, status, delay_minutes, reason) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, event.getLineId());
            ps.setString(2, event.getStatus());
            ps.setInt(3, event.getDelayMinutes());
            ps.setString(4, event.getReason());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("遅延イベントの保存に失敗しました", e);
        }
    }

    /** train_lines と delay_events を LEFT JOIN し、路線ごとに件数と平均遅延分数を集計する。 */
    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        String sql = "SELECT l.line_name, l.operator_name, "
                + "COUNT(e.id) AS total_delay_count, "
                + "IFNULL(AVG(e.delay_minutes), 0.0) AS avg_delay_minutes "
                + "FROM train_lines l "
                + "LEFT JOIN delay_events e ON l.id = e.line_id "
                + "GROUP BY l.id, l.line_name, l.operator_name "
                + "ORDER BY total_delay_count DESC";

        List<MorningPeakAnalyticsDTO> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MorningPeakAnalyticsDTO(
                        rs.getString("line_name"),
                        rs.getString("operator_name"),
                        rs.getInt("total_delay_count"),
                        rs.getDouble("avg_delay_minutes")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("集計クエリの実行に失敗しました", e);
        }
        return list;
    }
}
