package org.example.moneytransferservice;

import org.example.moneytransferservice.model.Amount;
import org.example.moneytransferservice.model.Transfer;
import org.example.moneytransferservice.repository.TransferRepository;
import org.example.moneytransferservice.repository.TransferState;
import org.example.moneytransferservice.service.TransferService;

import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitiateTransfer() {
        Transfer transfer = new Transfer();
        transfer.setCardFromNumber("1234567812345678");
        transfer.setCardToNumber("8765432187654321");
        transfer.setAmount(new Amount(100));

        String transferId = "transfer123";
        when(transferRepository.addTransfer(transfer)).thenReturn(transferId);

        String result = transferService.initiateTransfer(transfer);

        assertEquals(transferId, result);
        verify(transferRepository, times(1)).addTransfer(transfer);
    }

    @Test
    void testConfirmTransfer() {
        String transferId = "transfer123";

        // Создаем Transfer и инициализируем Amount
        Transfer transfer = new Transfer();
        transfer.setCardFromNumber("1234567812345678");
        transfer.setCardToNumber("8765432187654321");
        transfer.setAmount(new Amount(100));  // Инициализация Amount

        // Настройка мок-объекта
        when(transferRepository.confirmOperation(transferId)).thenReturn(transfer);

        // Вызов метода
        Transfer result = transferService.confirmTransfer(transferId);

        // Проверка результатов
        assertNotNull(result);
        assertEquals(transfer, result);  // Проверяем, что результат равен ожидаемому
        verify(transferRepository, times(1)).confirmOperation(transferId);
    }

    @Test
    void testGetTransferState() {
        String transferId = "transfer123";
        TransferState state = TransferState.OK;
        when(transferRepository.getTransferState(transferId)).thenReturn(state);

        TransferState result = transferService.getTransferState(transferId);

        assertEquals(state, result);
        verify(transferRepository, times(1)).getTransferState(transferId);
    }
}
