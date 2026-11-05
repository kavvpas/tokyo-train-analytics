package com.tokyo.train.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/** GET /api/train/delays で返す、保存済み遅延イベントの表示用 DTO。 */
@Data
@AllArgsConstructor
public class DelayEventViewDTO {
    private String lineName;
    private String status;
    private int delayMinutes;
    private String reason;
    private LocalDateTime occurredAt;
    private String source;
}
