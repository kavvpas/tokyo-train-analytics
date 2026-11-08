package com.tokyo.train.service;

import com.tokyo.train.engine.DelayCrawler;
import com.tokyo.train.model.entity.DelayEventEntity;
import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.repository.DelayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** TrainService の単体テスト。DB と外部 API は Mockito のモックに差し替える。 */
@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    DelayRepository delayRepository;

    @Mock
    DelayCrawler delayCrawler;

    @InjectMocks
    TrainService trainService;

    private static final LocalDateTime AT = LocalDateTime.of(2026, 11, 5, 8, 0);

    @BeforeEach
    void 路線マスタを用意する() {
        // lenient: このスタブを使わないテストがあってもエラーにしない
        org.mockito.Mockito.lenient().when(delayRepository.findAllLineIds())
                .thenReturn(Map.of("山手線", 1, "中央線", 2));
    }

    private static TrainDelayEntity entity(String line, String source, LocalDateTime occurredAt) {
        return new TrainDelayEntity(line, "遅延", 15, "人身事故の影響で遅延", source, occurredAt);
    }

    @Test
    void サンプルデータは保存しない() {
        // Arrange(準備)
        when(delayCrawler.crawlRealTimeData())
                .thenReturn(List.of(entity("山手線", "SAMPLE", null)));

        // Act(実行)
        int saved = trainService.fetchAndSaveRealTimeData();

        // Assert(検証)
        assertEquals(0, saved);
        verify(delayRepository, never()).saveEvent(any());
    }

    @Test
    void 路線マスタにない路線は保存しない() {
        when(delayCrawler.crawlRealTimeData())
                .thenReturn(List.of(entity("京浜東北線", "API", AT)));

        int saved = trainService.fetchAndSaveRealTimeData();

        assertEquals(0, saved);
        verify(delayRepository, never()).saveEvent(any());
    }

    @Test
    void 正常なデータは路線IDに変換して保存し_件数を返す() {
        when(delayCrawler.crawlRealTimeData()).thenReturn(List.of(
                entity("山手線", "API", AT),
                entity("中央線", "API", AT),
                entity("京浜東北線", "API", AT)));   // マスタにない → スキップ
        when(delayRepository.saveEvent(any())).thenReturn(true);

        int saved = trainService.fetchAndSaveRealTimeData();

        assertEquals(2, saved);
        // saveEvent に渡された引数を取り出して中身を検証する(ArgumentCaptor)
        ArgumentCaptor<DelayEventEntity> captor = ArgumentCaptor.forClass(DelayEventEntity.class);
        verify(delayRepository, times(2)).saveEvent(captor.capture());
        assertEquals(1, captor.getAllValues().get(0).getLineId());   // 山手線 → 1
        assertEquals(2, captor.getAllValues().get(1).getLineId());   // 中央線 → 2
        assertEquals("API", captor.getAllValues().get(0).getSource());
    }

    @Test
    void 重複データは件数に含めない() {
        when(delayCrawler.crawlRealTimeData())
                .thenReturn(List.of(entity("山手線", "API", AT)));
        // 唯一キーにより INSERT IGNORE が無視された状態(挿入 0 行)を false で再現する
        when(delayRepository.saveEvent(any())).thenReturn(false);

        int saved = trainService.fetchAndSaveRealTimeData();

        assertEquals(0, saved);
        verify(delayRepository, times(1)).saveEvent(any());   // 保存は試みている
    }
}
