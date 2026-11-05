package com.tokyo.train.engine;

import com.tokyo.train.model.entity.TrainDelayEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DelayCrawlerSourceTest {

    @Test
    void API未設定ならサンプルとして扱い発表日時はnull() {
        List<TrainDelayEntity> list = new DelayCrawler("").crawlRealTimeData();
        assertEquals(3, list.size());
        assertTrue(list.stream().allMatch(e -> "SAMPLE".equals(e.getSource())));
        assertTrue(list.stream().allMatch(e -> e.getOccurredAt() == null));
    }

    @Test
    void 発表日時は東京時間に変換される() {
        assertEquals(LocalDateTime.of(2026, 11, 5, 8, 30),
                DelayCrawler.parseOccurredAt("2026-11-04T23:30:00Z"));
        assertEquals(LocalDateTime.of(2026, 11, 5, 8, 30),
                DelayCrawler.parseOccurredAt("2026-11-05T08:30:00+09:00"));
    }

    @Test
    void 発表日時が空または不正ならnull() {
        assertNull(DelayCrawler.parseOccurredAt(null));
        assertNull(DelayCrawler.parseOccurredAt(""));
        assertNull(DelayCrawler.parseOccurredAt("昨日の朝"));
    }
}
