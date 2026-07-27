package com.tokyo.train.engine;

import com.tokyo.train.model.entity.TrainDelayEntity;
import com.tokyo.train.model.odpt.OdptTrainInfoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class DelayCrawler {

    public static List<TrainDelayEntity> crawlRealTimeData() {
        List<TrainDelayEntity> resultList = new ArrayList<>();
        List<OdptTrainInfoResponse> odptResponses = null;

        // 1. 尝试发起公网请求
        try {
            System.out.println("📡 [Spring Boot 3 RestClient] 正在尝试请求 ODPT 接口...");
            RestClient restClient = RestClient.create();
            odptResponses = restClient.get()
                    .uri("https://api.mock-tokyo.com/v4/odpt:TrainInformation")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<OdptTrainInfoResponse>>() {});
        } catch (Exception e) {
            System.out.println("⚠️ [网络降级保护触发] 远程接口不可达，全自动切入本地 ODPT 模拟数据流！");
            // 🌟 核心拦截：当公网不通时，全自动构造 2 条标准的 ODPT 官方格式数据！
            odptResponses = createMockOdptData();
        }

        // 2. 数据解析与组装流水线
        if (odptResponses != null) {
            for (OdptTrainInfoResponse res : odptResponses) {
                String rawLine = res.getRailway();
                String cleanLineName = "山手线"; // 默认保底

                if (rawLine != null) {
                    if (rawLine.contains("Yamanote")) cleanLineName = "山手线";
                    else if (rawLine.contains("ToeiOedo")) cleanLineName = "大江户线";
                    else if (rawLine.contains("Chuo")) cleanLineName = "中央线";
                }

                String rawMsg = (res.getTrainInformationText() != null) ? res.getTrainInformationText().getJa() : "正常";

                String status = "正常";
                int delayMinutes = 0;
                if (rawMsg.contains("遅延") || rawMsg.contains("影響")) {
                    status = "延迟";
                    delayMinutes = 15;
                } else if (rawMsg.contains("見合わせ") || rawMsg.contains("運休")) {
                    status = "停运";
                    delayMinutes = 45;
                }

                TrainDelayEntity entity = new TrainDelayEntity(cleanLineName, status, delayMinutes, rawMsg);
                resultList.add(entity);
            }
        }

        return resultList;
    }

    /**
     * 💡 本地 ODPT 模拟器：产生标准的 ODPT 官方数据
     */
    private static List<OdptTrainInfoResponse> createMockOdptData() {
        List<OdptTrainInfoResponse> list = new ArrayList<>();

        // 模拟山手线数据
        OdptTrainInfoResponse item1 = new OdptTrainInfoResponse();
        item1.setOperator("odpt.Operator:JR-East");
        item1.setRailway("odpt.Railway:JR-East.Yamanote");
        OdptTrainInfoResponse.TrainText text1 = new OdptTrainInfoResponse.TrainText();
        text1.setJa("山手線は、人身事故の影響で、遅延が生じています。");
        item1.setTrainInformationText(text1);

        // 模拟大江户线数据
        OdptTrainInfoResponse item2 = new OdptTrainInfoResponse();
        item2.setOperator("odpt.Operator:Toei");
        item2.setRailway("odpt.Railway:TokyoMetro.ToeiOedo");
        OdptTrainInfoResponse.TrainText text2 = new OdptTrainInfoResponse.TrainText();
        text2.setJa("大江戸線は、車両点検の影響で、運転を見合わせています。");
        item2.setTrainInformationText(text2);

        list.add(item1);
        list.add(item2);
        return list;
    }
}