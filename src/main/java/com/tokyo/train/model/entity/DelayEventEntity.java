package com.tokyo.train.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** delay_events テーブルの 1 行。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayEventEntity {
    private Long id;
    private Integer lineId; // train_lines.id への外部キー
    private String status;
    private Integer delayMinutes;
    private String reason;
    private LocalDateTime eventTime;   // 入庫時刻(DB が自動設定)
    private String source;              // "API" または "SAMPLE"
    private LocalDateTime occurredAt;   // 運行情報の発表日時

    /** 新規登録用(id と event_time は DB が自動採番・自動設定する)。 */
    public DelayEventEntity(Integer lineId, String status, Integer delayMinutes, String reason,
                            String source, LocalDateTime occurredAt) {
        this.lineId = lineId;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
        this.source = source;
        this.occurredAt = occurredAt;
    }
}
