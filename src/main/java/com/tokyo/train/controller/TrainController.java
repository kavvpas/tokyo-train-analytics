package com.tokyo.train.controller;

import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
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

    /**
     * 接口一：触发爬虫，抓取 ODPT 并自动多表落库
     * 访问地址：http://localhost:8080/api/train/delays
     */
    @GetMapping("/delays")
    public List<TrainDelayEntity> getDelays() {
        return trainService.fetchAndSaveRealTimeData();
    }

    /**
     * 接口二：🌟 核心商业分析接口，展示多表 JOIN 聚合算出来的早高峰延误报表！
     * 访问地址：http://localhost:8080/api/train/analytics/morning-peak
     */
    @GetMapping("/analytics/morning-peak")
    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        return trainService.getMorningPeakAnalytics();
    }
}