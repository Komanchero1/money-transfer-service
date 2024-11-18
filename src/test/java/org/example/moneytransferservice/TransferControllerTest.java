package org.example.moneytransferservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.moneytransferservice.controller.TransferController;
import org.example.moneytransferservice.logger.Logger;
import org.example.moneytransferservice.model.Amount;
import org.example.moneytransferservice.model.ConfirmInfo;
import org.example.moneytransferservice.model.Transfer;
import org.example.moneytransferservice.repository.TransferState;
import org.example.moneytransferservice.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//тестовый класс для проверки методов класса TransferController
public class TransferControllerTest {

    @Mock //аннотация, используется для создания мок-объекта класса TransferService с помощью библиотеки Mockito
    private TransferService transferService; //создается мок-объект TransferService
    private Logger logger; // Замокированный логгер

    //аннотация, используется для создания экземпляра класса TransferController
    // и автоматического внедрения в него мок-объектов, помеченных аннотацией @Mock
    @InjectMocks
    private TransferController transferController;//создается экземпляр тестируемого контроллера

    //создание объекта MockMvc позволяющего выполнять HTTP-запросы к контроллеру и проверять
    // ответы без необходимости разворачивания сервера
    private MockMvc mockMvc;

    @BeforeEach //фннотация, указывающая, что метод setUp будет выполняться перед каждым тестом
    void setUp() {
        MockitoAnnotations.openMocks(this);//инициализация мок-объектов, аннотированных в текущем классе
        //standaloneSetup позволяет настроить MockMvc для работы с конкретным контроллером
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    @Test//аннотация, указывающая, что метод testTransferMoney является тестовым методом
    //проверка работы метода TransferMoney
    public void testTransferMoney() throws Exception {
        Transfer transfer = new Transfer(); //создается объект Transfer
        // задаются значения обязательным полям
        transfer.setCardFromNumber("1234567812345678");//номер карты отправителя
        transfer.setCardFromValidTill("12/25");//срок действия карты отправителя
        transfer.setCardFromCVV("123");//код безопасности карты отправителя
        transfer.setCardToNumber("8765432187654321");//номер карты получателя
        transfer.setAmount(new Amount(100));//сумма перевода
        //настраивается поведение мок объекта,вызов метода initiateTransfer должен возвращать строку "transferId"
        when(transferService.initiateTransfer(any(Transfer.class))).thenReturn("transferId");

        // выполняется запрос и проверяется результат
        mockMvc.perform(post("/transfer")//выполняется POST-запрос к контроллеру на URL /transfer
                        .contentType(MediaType.APPLICATION_JSON) //содержимое запроса используется в формате JSON
                        .content(new ObjectMapper().writeValueAsString(transfer)))//преобразовывается transfer с помощью с помощью ObjectMapper в строку JSON
                .andExpect(status().isOk())//проверяется что статус ответа 200(ОК)
                .andDo(result -> {
                    System.out.println("Response: " + result.getResponse().getContentAsString());////выводится тело ответа в консоль
                });
    }


    @Test
    //тест метода confirmOperation
    public void testConfirmOperation() throws Exception {
        // создается объект ConfirmInfo который содержит индентифмкатор и код подтверждения
        ConfirmInfo confirmInfo = new ConfirmInfo("transferId", "123456");
        //настраивается мок-объект transferService чтобы при вызове confirmTransferс аргументом "transferId"
        // он возвращал объект Transfer
        when(transferService.confirmTransfer("transferId")).thenReturn(new Transfer());
        //выполняется пост запрос к контроллеру по URL /transfer/confirmOperation с телом запроса
        mockMvc.perform(post("/transfer/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)//содержимое запроса используется в формате JSON
                        .content(new ObjectMapper().writeValueAsString(confirmInfo)))//преобразовывается transfer с помощью с помощью ObjectMapper в строку JSON
                .andExpect(status().isOk());//проверяется что статус ответа равен 200(ОК)
    }



    @Test
    //тест метода getTransferState
    public void testGetTransferState() throws Exception {
        String transferId = "transferId";//создается индентификатор перевода
        //настраивается мок-объекта transferService, чтобы он возвращал состояние TransferState.OK,
        // когда будет вызван метод getTransferState с аргументом transferId
        when(transferService.getTransferState(transferId)).thenReturn(TransferState.OK);

        //выполняется GET-запрос к контроллеру по URL /transfer/state/{id}, подставляя transferId вместо {id}.
        mockMvc.perform(get("/transfer/state/{id}", transferId))
                .andExpect(status().isOk());//проверяется, что статус ответа равен 200 (OK).
    }

    @Test
    //тест метода getTransferState с несуществующим переводом
    public void testGetTransferStateNotFound() throws Exception {
        String transferId = "transferId";//создается индентификатор перевода
        //настраивается мок-объект transferService, чтобы он возвращал null, когда будет вызван метод getTransferState
        // с аргументом transferId
        when(transferService.getTransferState(transferId)).thenReturn(null);

        //выполняется GET-запрос к контроллеру по URL /transfer/state/{id}, подставляя transferId вместо {id}
        mockMvc.perform(get("/transfer/state/{id}", transferId))
                .andExpect(status().isNotFound());//проверяется, что статус ответа равен 404 (Not Found), перевод с указанным идентификатором не найден
    }
}