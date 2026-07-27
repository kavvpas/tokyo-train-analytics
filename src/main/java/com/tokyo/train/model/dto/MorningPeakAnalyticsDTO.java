package com.tokyo.train.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MorningPeakAnalyticsDTO {
    private String lineName;        // 线路名称（如 山手线）
    private String operatorName;    // 运营公司（如 JR-East）
    private Integer totalDelayCount; // 早高峰延误总次数
    private Double avgDelayMinutes;  // 平均延误分钟数
}
