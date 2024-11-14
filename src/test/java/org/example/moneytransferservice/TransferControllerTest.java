package org.example.moneytransferservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.moneytransferservice.controller.TransferController;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



public class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @Mock
    private TransferService transferService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    @Test
    public void testTransferMoney() throws Exception {
        // Arrange
        Transfer transfer = new Transfer();
        transfer.setCardFromNumber("1234567812345678");
        transfer.setCardFromValidTill("12/25");
        transfer.setCardFromCVV("123");
        transfer.setCardToNumber("8765432187654321");
        transfer.setAmount(new Amount(100));

        when(transferService.initiateTransfer(any(Transfer.class))).thenReturn("transferId");

        // Act & Assert
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    // Выводим тело ответа для отладки
                    System.out.println("Response: " + result.getResponse().getContentAsString());
                });
    }


    @Test
    public void testConfirmOperation() throws Exception {
        // Arrange
        ConfirmInfo confirmInfo = new ConfirmInfo("transferId", "123456");
        when(transferService.confirmTransfer("transferId")).thenReturn(new Transfer());

        // Act & Assert
        mockMvc.perform(post("/transfer/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(confirmInfo)))
                .andExpect(status().isOk());
    }

    @Test
    public void testConfirmOperationNotFound() throws Exception {
        // Arrange
        ConfirmInfo confirmInfo = new ConfirmInfo("transferId", "123456");
        when(transferService.confirmTransfer("transferId")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/transfer/confirmOperation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(confirmInfo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTransferState() throws Exception {
        // Arrange
        String transferId = "transferId";
        when(transferService.getTransferState(transferId)).thenReturn(TransferState.OK);

        // Act & Assert
        mockMvc.perform(get("/transfer/state/{id}", transferId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransferStateNotFound() throws Exception {
        // Arrange
        String transferId = "transferId";
        when(transferService.getTransferState(transferId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/transfer/state/{id}", transferId))
                .andExpect(status().isNotFound());
    }
}