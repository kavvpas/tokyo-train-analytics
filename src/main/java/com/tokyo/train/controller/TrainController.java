package com.tokyo.train.controller;

import com.tokyo.train.model.dto.DelayEventViewDTO;
import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.dto.Result;
import com.tokyo.train.service.TrainService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /** 保存済みの遅延イベントを新しい順に返す(読み取り専用なので GET)。limit は 1〜100(既定 50)。 */
    @GetMapping("/delays")
    public Result<List<DelayEventViewDTO>> getDelays(
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int limit) {
        return Result.success(trainService.getRecentDelays(limit));
    }

    /** 路線ごとの遅延件数・平均遅延分数を返す。 */
    @GetMapping("/analytics/morning-peak")
    public Result<List<MorningPeakAnalyticsDTO>> getMorningPeakAnalytics() {
        return Result.success(trainService.getMorningPeakAnalytics());
    }
}
