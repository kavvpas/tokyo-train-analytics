package com.tokyo.train.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainLineEntity {
    private Integer id;
    private String lineCode;
    private String lineName;
    private String operatorName;
    private LocalDateTime createTime;
}
