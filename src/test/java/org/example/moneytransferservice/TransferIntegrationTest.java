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

@SpringBootTest //аннотация указывает, что класс является интеграционным тестом Spring Boot
@AutoConfigureMockMvc //позволяет тестировать контроллеры в изолированной среде
//класс для интеграционного тестирования TransferController
public class TransferIntegrationTest {

    @Autowired  //аннотация, указывающая, что Spring должен автоматически внедрить зависимость - экземпляр MockMvc
    private MockMvc mockMvc;//для хранения объекта MockMvc который будет использоваться для выполнения HTTP-запросов в тестах



    @Test//аннотация, указывающая, что метод testTransferMoneyIntegration является тестовым методом
    //тест метода testTransferMoneyIntegration
    void testTransferMoneyIntegration() throws Exception {
        //выполняется POST-запрос к URL /transfer к контроллеру
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)//указывает, что содержимое запроса будет в формате JSON
                        .content("{\"cardFromNumber\":\"1234567812345678\"," +
                                "\"cardFromValidTill\":\"12/25\"," +
                                "\"cardFromCVV\":\"123\"," +
                                "\"cardToNumber\":\"8765432187654321\"," +
                                "\"amount\":{\"value\":100}}"))//добавляется тело запроса в формате JSON, представляющее данные перевода
                .andExpect(status().isOk())//проверяется, что статус ответа равен 200 (OK)
                .andExpect(jsonPath("$.operationId")
                        .exists());//проверяется, что в ответе существует поле operationId, что указывает на успешное создание перевода
    }

    @Test
    //Тест метода testConfirmOperation
    void testConfirmOperationIntegration() throws Exception {
        // инициализируется массив для хранения transferId чтобы менять значение индентификатора внутри лямбда-выражения
        final String[] transferId = new String[1];

        // выполняется POST-запрос к URL /transfer, который предназначен для инициации перевода.
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)//указывает, что содержимое запроса будет в формате JSON
                        .content("{\"cardFromNumber\":\"1234567812345678\"," +
                                "\"cardFromValidTill\":\"12/25\"," +
                                "\"cardFromCVV\":\"123\"," +
                                "\"cardToNumber\":\"8765432187654321\"," +
                                "\"amount\":{\"value\":100}}"))//указывается тело запроса, которое будет отправлено на сервер
                .andExpect(status().isOk())//проверяется, что статус ответа равен 200 (OK)
                .andExpect(jsonPath("$.operationId").exists()) // проверяется, что в ответе существует поле operationId, успешное создание перевода
                .andDo(result -> {
                    // извлечение значения поля operationId из тела ответа с помощью библиотеки JsonPath
                    transferId[0] = JsonPath.read(result.getResponse().getContentAsString(), "$.operationId");
                });

        // выполняется POST-запрос к контроллеру по URL /transfer/confirmOperation, который предназначен для подтверждения операции перевода
        mockMvc.perform(post("/transfer/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)//указывает, что содержимое запроса будет в формате JSON
                        .content("{\"operationId\":\"" + transferId[0] +
                                "\",\"code\":\"123456\"}"))//данные для подтверждения операции
                .andExpect(status().isOk())//проверяется, что статус ответа равен 200 (OK)
                .andExpect(jsonPath("$.operationId").value(transferId[0])); //проверяется, что поле operationId в ответе соответствует
    }

    @Test
    //ест метода testGetTransferState
    void testGetTransferStateIntegration() throws Exception {
        //выполняется POST-запрос к URL /transfer к контроллеру
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)//указывает, что содержимое запроса будет в формате JSON
                        .content("{\"cardFromNumber\":\"1234567812345678\"," +
                                "\"cardFromValidTill\":\"12/25\"," +
                                "\"cardFromCVV\":\"123\"," +
                                "\"cardToNumber\":\"8765432187654321\"," +
                                "\"amount\":{\"value\":100}}"))//добавляется тело запроса в формате JSON, представляющее данные перевода
                .andExpect(status().isOk())//проверяется, что статус ответа от сервера равен 200 (OK) запрос был успешно обработан
                .andExpect(jsonPath("$.operationId").exists()) //проверяется, что в ответе существует поле operationId, перевод успешен
                .andDo(result -> {
                    // извлечение значения поля operationId из тела ответа с помощью библиотеки JsonPath
                    String transferId = JsonPath.read(result.getResponse().getContentAsString(), "$.operationId");

                    // выполняется POST-запроса к контроллеру по URL /transfer/confirmOperation, который предназначен для подтверждения операции перевода
                    mockMvc.perform(post("/transfer/confirmOperation")
                                    .contentType(MediaType.APPLICATION_JSON)//указывает, что содержимое запроса будет в формате JSON
                                    .content("{\"operationId\":\"" +
                                            transferId + "\",\"code\":\"123456\"}"))//данные для подтверждения операции
                            .andExpect(status().isOk())//проверяется, что статус ответа равен 200 (OK)
                            .andExpect(jsonPath("$.operationId")
                                    .value(transferId));//проверяется, что поле operationId в ответе соответствует идентификатору transferId, который был получен ранее

                    // выполняется GET-запрос к контроллеру по URL /transfer/state/{id}, подставляя transferId вместо {id}
                    mockMvc.perform(get("/transfer/state/{id}", transferId))
                            .andExpect(status().isOk())//проверяется, что статус ответа равен 200 (OK)
                            .andExpect(content().json("\"OK\"")); //проверяется, что содержимое ответа соответствует строке "OK", что указывает на успешное состояние перевода
                });
    }

}
