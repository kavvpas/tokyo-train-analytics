package com.tokyo.train.repository;

import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.entity.DelayEventEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DelayRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/test_db";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // 🌟 记得改成你的实际数据库密码！

    /**
     * 1. 辅助方法：根据线路名称查询 train_lines 表的主键 id
     */
    public Integer findLineIdByName(String lineName) {
        String sql = "SELECT id FROM train_lines WHERE line_name LIKE ? LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + lineName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("🚨 查询线路 ID 失败: " + e.getMessage());
        }
        // 如果没查到，默认归类到 1 号线路（比如山手线），防止程序崩溃
        return 1;
    }

    /**
     * 2. 写入延误事件到 delay_events 子表
     */
    public void saveEvent(DelayEventEntity event) {
        String sql = "INSERT INTO delay_events (line_id, status, delay_minutes, reason) VALUES (?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, event.getLineId());
            ps.setString(2, event.getStatus());
            ps.setInt(3, event.getDelayMinutes());
            ps.setString(4, event.getReason());
            ps.executeUpdate();
            System.out.println("✅ [多表落库成功] 延误事件已成功写入 delay_events 表！");
        } catch (SQLException e) {
            System.out.println("🚨 写入延误事件失败: " + e.getMessage());
        }
    }

    /**
     * 3. 🌟 核心硬核接口：执行多表 LEFT JOIN 聚合 SQL，直接封装成 DTO 返回！
     */
    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        List<MorningPeakAnalyticsDTO> analyticsList = new ArrayList<>();

        // 这就是我们之前在 DBeaver 里的多表聚合 SQL！
        String sql = "SELECT " +
                "    l.line_name, " +
                "    l.operator_name, " +
                "    COUNT(e.id) AS total_delay_count, " +
                "    IFNULL(AVG(e.delay_minutes), 0.0) AS avg_delay_minutes " +
                "FROM train_lines l " +
                "LEFT JOIN delay_events e ON l.id = e.line_id " +
                "GROUP BY l.id, l.line_name, l.operator_name " +
                "ORDER BY total_delay_count DESC;";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 逐行把 SQL 算好的统计结果，塞进我们的 DTO 箩筐里！
                MorningPeakAnalyticsDTO dto = new MorningPeakAnalyticsDTO(
                        rs.getString("line_name"),
                        rs.getString("operator_name"),
                        rs.getInt("total_delay_count"),
                        rs.getDouble("avg_delay_minutes")
                );
                analyticsList.add(dto);
            }
        } catch (SQLException e) {
            System.out.println("🚨 多表聚合查询失败: " + e.getMessage());
        }

        return analyticsList;
    }
}