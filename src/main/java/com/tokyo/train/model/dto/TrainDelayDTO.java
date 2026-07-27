package com.tokyo.train.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               // 自动搞定 Getter/Setter/toString
@NoArgsConstructor  // 自动搞定无参构造
@AllArgsConstructor // 自动搞定全参构造：public TrainDelayDTO(String lineName...)
public class TrainDelayDTO {
    private String lineName;
    private String status;
    private Integer delayMinutes;
    private String reason;
}