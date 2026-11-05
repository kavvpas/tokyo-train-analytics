package com.tokyo.train.controller;

import com.tokyo.train.model.dto.DelayEventViewDTO;
import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.dto.Result;
import com.tokyo.train.service.TrainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    /** 遅延情報を取得して DB に保存する(状態を変更するので POST)。保存した件数を返す。 */
    @PostMapping("/delays/fetch")
    public Result<Integer> fetchDelays() {
        return Result.success(trainService.fetchAndSaveRealTimeData());
    }

    /** 保存済みの遅延イベントを返す(読み取り専用なので GET)。 */
    @GetMapping("/delays")
    public Result<List<DelayEventViewDTO>> getDelays() {
        return Result.success(trainService.getRecentDelays());
    }

    /** 路線ごとの遅延件数・平均遅延分数を返す。 */
    @GetMapping("/analytics/morning-peak")
    public Result<List<MorningPeakAnalyticsDTO>> getMorningPeakAnalytics() {
        return Result.success(trainService.getMorningPeakAnalytics());
    }
}
