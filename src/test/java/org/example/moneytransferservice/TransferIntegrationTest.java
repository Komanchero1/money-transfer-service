package org.example.moneytransferservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    void testTransferMoneyIntegration() throws Exception {
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardFromNumber\":\"1234567812345678\",\"cardFromValidTill\":\"12/25\",\"cardFromCVV\":\"123\",\"cardToNumber\":\"8765432187654321\",\"amount\":{\"value\":100}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").exists());
    }

    @Test
    void testConfirmOperationIntegration() throws Exception {
        // Инициализируем массив для хранения ID
        final String[] transferId = new String[1];

        // Сначала инициируем перевод
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardFromNumber\":\"1234567812345678\",\"cardFromValidTill\":\"12/25\",\"cardFromCVV\":\"123\",\"cardToNumber\":\"8765432187654321\",\"amount\":{\"value\":100}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").exists()) // Убедитесь, что ID существует
                .andDo(result -> {
                    // Извлекаем ID из результата
                    transferId[0] = JsonPath.read(result.getResponse().getContentAsString(), "$.operationId");
                });

        // Теперь подтверждаем операцию
        mockMvc.perform(post("/transfer/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"operationId\":\"" + transferId[0] + "\",\"code\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value(transferId[0])); // Используем динамически полученный ID
    }

    @Test
    void testGetTransferStateIntegration() throws Exception {
        // Инициализируем перевод
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardFromNumber\":\"1234567812345678\",\"cardFromValidTill\":\"12/25\",\"cardFromCVV\":\"123\",\"cardToNumber\":\"8765432187654321\",\"amount\":{\"value\":100}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").exists()) // Убедитесь, что ID существует
                .andDo(result -> {
                    // Извлекаем ID из результата
                    String transferId = JsonPath.read(result.getResponse().getContentAsString(), "$.operationId");

                    // Подтверждаем операцию
                    mockMvc.perform(post("/transfer/confirmOperation")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"operationId\":\"" + transferId + "\",\"code\":\"123456\"}"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.operationId").value(transferId));

                    // Теперь проверяем состояние перевода
                    mockMvc.perform(get("/transfer/state/{id}", transferId))
                            .andExpect(status().isOk())
                            .andExpect(content().json("\"OK\"")); // Проверяем состояние как JSON
                });
    }

}
