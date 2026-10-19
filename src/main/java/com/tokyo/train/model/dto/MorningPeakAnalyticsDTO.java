package com.tokyo.train.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 路線ごとの遅延集計結果。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MorningPeakAnalyticsDTO {
    private String lineName;          // 路線名(例: 山手線)
    private String operatorName;      // 運営会社(例: JR東日本)
    private Integer totalDelayCount;  // 遅延イベント件数
    private Double avgDelayMinutes;   // 平均遅延分数
}
