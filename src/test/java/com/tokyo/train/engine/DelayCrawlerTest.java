package com.tokyo.train.engine;

import com.tokyo.train.model.entity.TrainDelayEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DelayCrawlerTest {

    @Test
    void 遅延キーワードなら遅延15分() {
        var c = DelayCrawler.classify("山手線は、人身事故の影響で、遅延が生じています。");
        assertEquals("遅延", c.status());
        assertEquals(15, c.delayMinutes());
    }

    @Test
    void 見合わせなら運休45分() {
        var c = DelayCrawler.classify("大江戸線は、車両点検の影響で、運転を見合わせています。");
        assertEquals("運休", c.status());
        assertEquals(45, c.delayMinutes());
    }

    @Test
    void キーワードなしは平常運転() {
        var c = DelayCrawler.classify("現在、平常どおり運転しています。");
        assertEquals("平常運転", c.status());
        assertEquals(0, c.delayMinutes());
    }

    @Test
    void 路線IDを路線名に変換できる() {
        assertEquals("山手線", DelayCrawler.toLineName("odpt.Railway:JR-East.Yamanote"));
        assertNull(DelayCrawler.toLineName("odpt.Railway:Unknown"));
        assertNull(DelayCrawler.toLineName(null));
    }

    @Test
    void API未設定ならサンプルデータ3件を返す() {
        List<TrainDelayEntity> list = new DelayCrawler("").crawlRealTimeData();
        assertEquals(3, list.size());
        assertEquals("山手線", list.get(0).getLineName());
    }
}
