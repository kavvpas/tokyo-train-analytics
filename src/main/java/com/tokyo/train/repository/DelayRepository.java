package com.tokyo.train.repository;

import com.tokyo.train.model.dto.DelayEventViewDTO;
import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.entity.DelayEventEntity;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** JDBC(PreparedStatement)で MySQL にアクセスする。 */
@Repository
public class DelayRepository {

    private final DataSource dataSource;

    public DelayRepository(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

    /** train_lines の全路線を 1 回のクエリで取得し、路線名 → id の Map にして返す。 */
    public Map<String, Integer> findAllLineIds() {
        String sql = "SELECT id, line_name FROM train_lines";
        Map<String, Integer> map = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("line_name"), rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("路線一覧の取得に失敗しました", e);
        }
        return map;
    }

    /**
     * 遅延イベントを delay_events に保存する。
     * 同じ (line_id, occurred_at, reason) が既にあれば無視する(唯一キー uk_event)。
     * @return 実際に挿入されたら true、重複で無視されたら false
     */
    public boolean saveEvent(DelayEventEntity event) {
        String sql = "INSERT IGNORE INTO delay_events "
                + "(line_id, status, delay_minutes, reason, source, occurred_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, event.getLineId());
            ps.setString(2, event.getStatus());
            ps.setInt(3, event.getDelayMinutes());
            ps.setString(4, event.getReason() == null ? "" : event.getReason());
            ps.setString(5, event.getSource());
            ps.setObject(6, event.getOccurredAt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("遅延イベントの保存に失敗しました", e);
        }
    }

    /** train_lines と delay_events を LEFT JOIN し、路線ごとに件数と平均遅延分数を集計する。 */
    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        String sql = """
    SELECT l.line_name, l.operator_name,
           COUNT(e.id) AS total_delay_count,
           IFNULL(AVG(e.delay_minutes), 0.0) AS avg_delay_minutes
    FROM train_lines l
    LEFT JOIN delay_events e
      ON l.id = e.line_id
     AND e.delay_minutes > 0
     AND HOUR(e.occurred_at) >= 7
     AND HOUR(e.occurred_at) < 9
    GROUP BY l.id, l.line_name, l.operator_name
    ORDER BY total_delay_count DESC
    """;
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

    /** 保存済みの遅延イベントを新しい順に最大 50 件取得する(読み取り専用)。 */
    public List<DelayEventViewDTO> findRecentEvents() {
        String sql = """
                SELECT l.line_name, e.status, e.delay_minutes, e.reason, e.occurred_at, e.source
                FROM delay_events e
                JOIN train_lines l ON l.id = e.line_id
                ORDER BY e.occurred_at DESC, e.id DESC
                LIMIT 50
                """;
        List<DelayEventViewDTO> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DelayEventViewDTO(
                        rs.getString("line_name"),
                        rs.getString("status"),
                        rs.getInt("delay_minutes"),
                        rs.getString("reason"),
                        rs.getTimestamp("occurred_at").toLocalDateTime(),
                        rs.getString("source")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("遅延イベントの取得に失敗しました", e);
        }
        return list;
    }
}
