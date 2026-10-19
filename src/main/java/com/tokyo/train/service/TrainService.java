package com.tokyo.train.service;

import com.tokyo.train.engine.DelayCrawler;
import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.entity.DelayEventEntity;
import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.repository.DelayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private static final Logger log = LoggerFactory.getLogger(TrainService.class);

    private final DelayRepository delayRepository;
    private final DelayCrawler delayCrawler;

    public TrainService(DelayRepository delayRepository, DelayCrawler delayCrawler) {
        this.delayRepository = delayRepository;
        this.delayCrawler = delayCrawler;
    }

    /** 遅延情報を取得し、路線マスタに存在する路線だけ delay_events に保存する。 */
    public List<TrainDelayEntity> fetchAndSaveRealTimeData() {
        List<TrainDelayEntity> rawData = delayCrawler.crawlRealTimeData();

        for (TrainDelayEntity entity : rawData) {
            Integer lineId = delayRepository.findLineIdByName(entity.getLineName());
            if (lineId == null) {
                log.warn("路線マスタに存在しないためスキップ: {}", entity.getLineName());
                continue;
            }
            delayRepository.saveEvent(new DelayEventEntity(
                    lineId, entity.getStatus(), entity.getDelayMinutes(), entity.getReason()));
        }
        return rawData;
    }

    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        return delayRepository.getMorningPeakAnalytics();
    }
}
