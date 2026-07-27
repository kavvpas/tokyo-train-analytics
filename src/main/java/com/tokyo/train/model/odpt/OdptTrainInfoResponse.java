package com.tokyo.train.model.odpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OdptTrainInfoResponse {

    // 🌟 核心一枪：利用 @JsonProperty 注解，完美把带冒号的官方 JSON 字段，强行映射进我们干净的 Java 变量里！
    @JsonProperty("odpt:operator")
    private String operator;

    @JsonProperty("odpt:railway")
    private String railway;

    @JsonProperty("odpt:trainInformationText")
    private TrainText trainInformationText;

    // 内部静态类：用来套娃解析官方的 {"ja": "...", "en": "..."} 结构
    @Data
    public static class TrainText {
        private String ja;
        private String en;
    }
}
