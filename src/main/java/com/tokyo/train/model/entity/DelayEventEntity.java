package com.tokyo.train.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayEventEntity {
    private Long id;
    private Integer lineId; // 🌟 核心关联：指向 train_lines 主表的外键 ID
    private String status;
    private Integer delayMinutes;
    private String reason;
    private LocalDateTime eventTime;

    // 快捷构造方法：方便爬虫抓到数据后组装入库（不带 id 和时间，交给 MySQL 全自动生成）
    public DelayEventEntity(Integer lineId, String status, Integer delayMinutes, String reason) {
        this.lineId = lineId;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
    }
}
