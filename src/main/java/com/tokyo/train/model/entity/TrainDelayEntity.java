package com.tokyo.train.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** 取得した遅延情報 1 件(DB 保存前の中間オブジェクト)。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainDelayEntity {

    private Integer id;
    private String lineName;
    private String status;
    private Integer delayMinutes;
    private String reason;
    private String source;            // "API" または "SAMPLE"
    private LocalDateTime occurredAt; // 運行情報の発表日時(不明なら null)

    public TrainDelayEntity(String lineName, String status, Integer delayMinutes, String reason,
                            String source, LocalDateTime occurredAt) {
        this.lineName = lineName;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
        this.source = source;
        this.occurredAt = occurredAt;
    }
}
