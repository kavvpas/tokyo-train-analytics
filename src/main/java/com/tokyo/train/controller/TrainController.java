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

    @GetMapping("/delays")
    public Result<List<TrainDelayEntity>> getDelays() {
        List<TrainDelayEntity> data = trainService.fetchAndSaveRealTimeData();
        return Result.success(data); // 🌟 用 Result.success() 统一包裹！
    }

    @GetMapping("/analytics/morning-peak")
    public Result<List<MorningPeakAnalyticsDTO>> getMorningPeakAnalytics() {
        List<MorningPeakAnalyticsDTO> data = trainService.getMorningPeakAnalytics();
        return Result.success(data); // 🌟 用 Result.success() 统一包裹！
    }
}