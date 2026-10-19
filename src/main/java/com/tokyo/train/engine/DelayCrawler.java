package com.tokyo.train.engine;

import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.model.odpt.OdptTrainInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 遅延情報を取得して TrainDelayEntity に変換する。
 * odpt.api-url が設定されていれば API から取得し、未設定または失敗した場合は
 * 内蔵のサンプルデータ(ODPT 形式)にフォールバックする。
 */
@Component
public class DelayCrawler {

    private static final Logger log = LoggerFactory.getLogger(DelayCrawler.class);

    private final String apiUrl;

    public DelayCrawler(@Value("${odpt.api-url:}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<TrainDelayEntity> crawlRealTimeData() {
        List<OdptTrainInfoResponse> responses = fetchFromApi();
        if (responses == null) {
            log.info("API 未設定または取得失敗のため、サンプルデータを使用します");
            responses = createSampleData();
        }

        List<TrainDelayEntity> result = new ArrayList<>();
        for (OdptTrainInfoResponse res : responses) {
            String lineName = toLineName(res.getRailway());
            String text = (res.getTrainInformationText() != null && res.getTrainInformationText().getJa() != null)
                    ? res.getTrainInformationText().getJa() : "";
            Classification c = classify(text);
            result.add(new TrainDelayEntity(lineName, c.status(), c.delayMinutes(), text));
        }
        return result;
    }

    private List<OdptTrainInfoResponse> fetchFromApi() {
        if (apiUrl == null || apiUrl.isBlank()) {
            return null;
        }
        try {
            return RestClient.create().get()
                    .uri(apiUrl)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<OdptTrainInfoResponse>>() {});
        } catch (Exception e) {
            log.warn("遅延情報 API の取得に失敗しました: {}", e.getMessage());
            return null;
        }
    }

    /** 本文のキーワードから状態と遅延分数を推定する(簡易ルール)。 */
    public static Classification classify(String text) {
        if (text.contains("見合わせ") || text.contains("運休")) {
            return new Classification("運休", 45);
        }
        if (text.contains("遅延") || text.contains("影響")) {
            return new Classification("遅延", 15);
        }
        return new Classification("平常運転", 0);
    }

    /** railway ID から路線名へ変換する。対応は 3 路線のみ。不明な場合は null。 */
    static String toLineName(String railway) {
        if (railway == null) return null;
        if (railway.contains("Yamanote")) return "山手線";
        if (railway.contains("ToeiOedo")) return "大江戸線";
        if (railway.contains("Chuo")) return "中央線";
        return null;
    }

    public record Classification(String status, int delayMinutes) {}

    private static List<OdptTrainInfoResponse> createSampleData() {
        List<OdptTrainInfoResponse> list = new ArrayList<>();
        list.add(sample("odpt.Operator:JR-East", "odpt.Railway:JR-East.Yamanote",
                "山手線は、人身事故の影響で、遅延が生じています。"));
        list.add(sample("odpt.Operator:Toei", "odpt.Railway:TokyoMetro.ToeiOedo",
                "大江戸線は、車両点検の影響で、運転を見合わせています。"));
        list.add(sample("odpt.Operator:JR-East", "odpt.Railway:JR-East.Chuo",
                "現在、平常どおり運転しています。"));
        return list;
    }

    private static OdptTrainInfoResponse sample(String operator, String railway, String ja) {
        OdptTrainInfoResponse r = new OdptTrainInfoResponse();
        r.setOperator(operator);
        r.setRailway(railway);
        OdptTrainInfoResponse.TrainText t = new OdptTrainInfoResponse.TrainText();
        t.setJa(ja);
        r.setTrainInformationText(t);
        return r;
    }
}
