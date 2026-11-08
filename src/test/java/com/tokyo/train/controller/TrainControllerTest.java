package com.tokyo.train.controller;

import com.tokyo.train.service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Controller 層のテスト。Service はモックにし、HTTP の入出力(ステータスと JSON)だけを検証する。 */
@WebMvcTest(TrainController.class)
class TrainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrainService trainService;

    @Test
    void limit未指定なら既定の50件で取得する() throws Exception {
        when(trainService.getRecentDelays(50)).thenReturn(List.of());

        mockMvc.perform(get("/api/train/delays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(trainService).getRecentDelays(50);
    }

    @Test
    void limitが範囲外なら400と共通形式のJSONを返す() throws Exception {
        mockMvc.perform(get("/api/train/delays").param("limit", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));

        verifyNoInteractions(trainService);   // 検証エラーなら Service は呼ばれない
    }

    @Test
    void limitが数値でなければ400() throws Exception {
        mockMvc.perform(get("/api/train/delays").param("limit", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void POSTの取得処理は保存件数を返す() throws Exception {
        when(trainService.fetchAndSaveRealTimeData()).thenReturn(3);

        mockMvc.perform(post("/api/train/delays/fetch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(3));
    }
}
