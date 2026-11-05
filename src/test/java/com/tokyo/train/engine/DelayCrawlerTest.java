package com.tokyo.train.engine;

import com.tokyo.train.model.entity.TrainDelayEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DelayCrawlerClassifyTest {

    @ParameterizedTest
    @CsvSource({
            "山手線は、人身事故の影響で、遅延が生じています。, 遅延, 15",
            "大江戸線は、車両点検の影響で、運転を見合わせています。, 運休, 45",
            "現在、平常どおり運転しています。, 平常運転, 0",
            "遅延の影響はありません。, 平常運転, 0",
            "運転を見合わせていましたが、再開しました。, 平常運転, 0"
    })
    void 本文から状態と遅延分数を判定する(String text, String expectedStatus, int expectedMinutes) {
        var c = DelayCrawler.classify(text);
        assertEquals(expectedStatus, c.status());
        assertEquals(expectedMinutes, c.delayMinutes());
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
