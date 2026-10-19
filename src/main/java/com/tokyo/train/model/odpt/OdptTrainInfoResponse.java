package com.tokyo.train.model.odpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** ODPT 形式(odpt:TrainInformation)の JSON を受け取るクラス。 */
@Data
public class OdptTrainInfoResponse {

    // "odpt:operator" のようにコロンを含むキーを @JsonProperty で Java のフィールドに対応させる
    @JsonProperty("odpt:operator")
    private String operator;

    @JsonProperty("odpt:railway")
    private String railway;

    @JsonProperty("odpt:trainInformationText")
    private TrainText trainInformationText;

    /** {"ja": "...", "en": "..."} 形式の本文。 */
    @Data
    public static class TrainText {
        private String ja;
        private String en;
    }
}
