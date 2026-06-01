package com.tokyo.train.service;

import com.tokyo.train.engine.DelayCrawler;
import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.entity.DelayEventEntity;
import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.repository.DelayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    @Autowired
    private DelayRepository delayRepository;

    /**
     * 1. 从 ODPT 抓取实时数据，并自动去主表查 line_id 存入 delay_events 表
     */
    public List<TrainDelayEntity> fetchAndSaveRealTimeData() {
        List<TrainDelayEntity> rawData = DelayCrawler.crawlRealTimeData();

        for (TrainDelayEntity entity : rawData) {
            // 🌟 步骤 A：去 train_lines 主表查到这条线路的 id（比如山手线查出 1）
            Integer lineId = delayRepository.findLineIdByName(entity.getLineName());

            // 🌟 步骤 B：把数据封装成子表实体，落库存进 delay_events 表
            DelayEventEntity event = new DelayEventEntity(
                    lineId,
                    entity.getStatus(),
                    entity.getDelayMinutes(),
                    entity.getReason()
            );
            delayRepository.saveEvent(event);
        }

        return rawData;
    }

    /**
     * 2. 🌟 核心新功能：调用 Repository 的多表 LEFT JOIN 聚合 SQL，提取早高峰分析报表
     */
    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        return delayRepository.getMorningPeakAnalytics();
    }
}