package com.tokyo.train.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public TrainDelayEntity(String lineName, String status, Integer delayMinutes, String reason) {
        this.lineName = lineName;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
    }
}
