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


//тестовый класс для проверки функциональности класса TransferService
public class TransferServiceTest {

    @Mock // аннотация указывает что буден создан мок объект для имитации объекта TransferRepository
    private TransferRepository transferRepository; // для хранения мок объекта

    @InjectMocks //Аннотация,укказывающая что булет создан экземпляр класса TransferService с автоматическим внедрением в него мок-объекта
    private TransferService transferService; //для хранения экземпляра тестируемого сервиса

    @BeforeEach //Аннотация, указывающая, что метод setUp будет выполняться перед каждым тестом
    //метод инициализирующий мок-объектоы, аннотированные в текущем классе
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test //указывает что метод тестовый
    //тестирование метода InitiateTransfer из класса TransferService
    void testInitiateTransfer() {
        Transfer transfer = new Transfer(); //создание объекта Transfer
        transfer.setCardFromNumber("1234567812345678");//устанавливается номер карты отправителя
        transfer.setCardToNumber("8765432187654321");//устанавливается номер карты получателя
        transfer.setAmount(new Amount(100));//устанавливается сумма перевода

        String transferId = "transfer123";//устанавливается индентификатор перевода

        //настройка поведения мок-объекта transferRepository
        when(transferRepository.addTransfer(transfer)).thenReturn(transferId);//при вызове метода addTransfer с аргументом transfer, он должен вернуть transferId

        //вызывается метод initiateTransfer и результат записывается в переменную
        String result = transferService.initiateTransfer(transfer);

        assertEquals(transferId, result);//сравнивается ожидаемый и фактический результат
        //проверка, что метод addTransfer был вызван ровно один раз с аргументом transfer на мок-объекте transferRepository
        verify(transferRepository, times(1)).addTransfer(transfer);
    }

    @Test
        //тестирование метода ConfirmTransfer из класса TransferService
    void testConfirmTransfer() {
        String transferId = "transfer123";//устанавливается индентификатор операции
        Transfer transfer = new Transfer();// создается объект Transfer
        transfer.setCardFromNumber("1234567812345678");//устанавливается номер карты отправителя
        transfer.setCardToNumber("8765432187654321");//устанавливается номер карты получателя
        transfer.setAmount(new Amount(100));  // Инициализация Amount

        // настраивается  поведения мок-объекта при вызове confirmOperation возвращается индентификатор операции
        when(transferRepository.confirmOperation(transferId)).thenReturn(transfer);

        // вызывается метод confirmTransfer результат сохраняется в переменную
        Transfer result = transferService.confirmTransfer(transferId);

        assertNotNull(result);//проверяется что результат не равен null
        assertEquals(transfer, result);  // Проверяется, что результат равен ожидаемому
        //Проверяется, что метод confirmOperation был вызван ровно один раз с аргументом transferId на мок-объекте transferRepository
        verify(transferRepository, times(1)).confirmOperation(transferId);
    }

    @Test
    //тестирование метода GetTransferState из класса TransferService
    void testGetTransferState() {
        String transferId = "transfer123"; //устанавливается индентификатор операции
        TransferState state = TransferState.OK;//устанавливается статус операции
        //настраивается поведение мок объекта при вызове getTransferState возвращается статус операции
        when(transferRepository.getTransferState(transferId)).thenReturn(state);

        TransferState result = transferService.getTransferState(transferId);//вызывается тестируемый метод и сохраняется в переменную

        assertEquals(state, result);//проверяется что возвращенное состояние result соответствует ожидаемому состоянию state
        //проверяется что метод getTransferState был вызван ровно один раз с аргументом transferId на мок-объекте transferRepository
        verify(transferRepository, times(1)).getTransferState(transferId);
    }
}
