package com.tokyo.train.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               // 🌟 终极神注：全自动生成所有属性的 Getter、Setter、toString、equals 方法！
@NoArgsConstructor  // 🌟 全自动生成无参构造方法：public TrainDelayEntity() {}
@AllArgsConstructor // 🌟 全自动生成包含所有属性的构造方法（带ID的那个）
public class TrainDelayEntity {

    private Integer id;
    private String lineName;
    private String status;
    private Integer delayMinutes;
    private String reason;

    // 💡 只有这个特殊的、不带 ID 的构造方法（专门给爬虫用的），需要我们单独保留
    public TrainDelayEntity(String lineName, String status, Integer delayMinutes, String reason) {
        this.lineName = lineName;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.reason = reason;
    }
}