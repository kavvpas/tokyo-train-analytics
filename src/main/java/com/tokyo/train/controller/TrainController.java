package com.tokyo.train.controller;

import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.dto.Result;
import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.service.TrainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/train")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    /** 遅延情報を取得して DB に保存し、取得内容を返す。 */
    @GetMapping("/delays")
    public Result<List<TrainDelayEntity>> getDelays() {
        return Result.success(trainService.fetchAndSaveRealTimeData());
    }

    /** 路線ごとの遅延件数・平均遅延分数を返す。 */
    @GetMapping("/analytics/morning-peak")
    public Result<List<MorningPeakAnalyticsDTO>> getMorningPeakAnalytics() {
        return Result.success(trainService.getMorningPeakAnalytics());
    }
}
