package com.tokyo.train.service;

import com.tokyo.train.engine.DelayCrawler;
import com.tokyo.train.model.dto.DelayEventViewDTO;
import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.model.dto.MorningPeakAnalyticsDTO;
import com.tokyo.train.model.entity.DelayEventEntity;
import com.tokyo.train.repository.DelayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TrainService {

    private static final Logger log = LoggerFactory.getLogger(TrainService.class);

    private final DelayRepository delayRepository;
    private final DelayCrawler delayCrawler;

    public TrainService(DelayRepository delayRepository, DelayCrawler delayCrawler) {
        this.delayRepository = delayRepository;
        this.delayCrawler = delayCrawler;
    }

    /** 遅延情報を取得し、路線マスタに存在する路線だけ delay_events に保存する。新しく保存した件数を返す(重複・サンプルは含まない)。 */
    @Transactional
    public int fetchAndSaveRealTimeData() {
        List<TrainDelayEntity> rawData = delayCrawler.crawlRealTimeData();
        int saved = 0;
        // 路線マスタは 1 回のクエリで取得し、ループ内では DB に問い合わせない
        Map<String, Integer> lineIds = delayRepository.findAllLineIds();

        for (TrainDelayEntity entity : rawData) {
            // サンプルデータや発表日時が不明のデータは保存しない(統計を汚さない)
            if ("SAMPLE".equals(entity.getSource()) || entity.getOccurredAt() == null) {
                log.info("保存対象外(source={}, occurredAt={}): {}",
                        entity.getSource(), entity.getOccurredAt(), entity.getLineName());
                continue;
            }
            Integer lineId = lineIds.get(entity.getLineName());
            if (lineId == null) {
                log.warn("路線マスタに存在しないためスキップ: {}", entity.getLineName());
                continue;
            }
            boolean inserted = delayRepository.saveEvent(new DelayEventEntity(
                    lineId, entity.getStatus(), entity.getDelayMinutes(), entity.getReason(),
                    entity.getSource(), entity.getOccurredAt()));
            if (inserted) saved++;
        }
        return saved;
    }

    /** 保存済みの遅延イベントを読み取るだけ(DB を変更しない)。 */
    @Transactional(readOnly = true)
    public List<DelayEventViewDTO> getRecentDelays(int limit) {
        return delayRepository.findRecentEvents(limit);
    }

    public List<MorningPeakAnalyticsDTO> getMorningPeakAnalytics() {
        return delayRepository.getMorningPeakAnalytics();
    }
}
