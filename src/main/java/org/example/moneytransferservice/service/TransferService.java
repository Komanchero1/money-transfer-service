package org.example.moneytransferservice.service;

import org.example.moneytransferservice.exception.InvalidDataException;
import org.example.moneytransferservice.logger.Logger;
import org.example.moneytransferservice.logger.LoggerImpl;
import org.example.moneytransferservice.model.Transfer;
import org.example.moneytransferservice.repository.TransferRepository;
import org.example.moneytransferservice.repository.TransferState;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Service // клас помечается как сервис который будет управлять логикой перевода денег
public class TransferService {
    private final TransferRepository transferRepository;// поле которое используется для доступа к данным о переводах
    private final Logger logger;//поле для логирования

    //конструктор принимающий в качестве параметра объект transferRepository дающий доступ к методам управляющим переводами
    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository; //инициализируем поле transferRepository
        this.logger = LoggerImpl.getInstance(); //получаем экземпляр логера
    }

    //метод для инициализации перевода
    public String initiateTransfer(Transfer transfer) { // в качестве аргумента принимается объект transfer
        String transferId = transferRepository.addTransfer(transfer); //получается индентификатор операции
        logTransfer(transfer, transferId, " Инициализированно ", null); //логируем инициализацию
        return transferId; //возвращается индентификатор нового перевода
    }

    //метод для подтверждения перевода
    public Transfer confirmTransfer(String transferId) { //в качестве параметра получает индентификатор перевода
        Transfer transfer = transferRepository.confirmOperation(transferId); //получается и сохраняется индентификатор перевода
        if (transfer == null) { // если объект с нужным индентификатором не найден
            logger.log(" ID " + transferId + " не найден для подтверждения.");//логируется ошибка
            throw new InvalidDataException("не найден ID: " + transferId);//выбрасывается сообщение об ошибке
        }
        logTransfer(transfer, transferId, "Подтвержденный", null);//записывается лог подтверждения перевода
        return transfer; //возврашается объект трансфер соответствующий индентификатору
    }

    //метод для получения состояния перевода
    public TransferState getTransferState(String transferId) { //в качестве параметра получает индентификатор перевода
        return transferRepository.getTransferState(transferId);// возвращает состояние перевода
    }

    //метод для логирования перевода
    //в качестве параметра принимается объект Transfer, идентификатор перевода, статус перевода и комиссию
    private void logTransfer(Transfer transfer, String transferId, String status, Double commission) {
        //формат строки логирования
        String logMessage = String.format("Date: %s, Time: %s, Transfer ID: %s, From Card: %s, To Card: %s, Amount: %d, Commission: %s, Status: %s",
                LocalDate.now(),//получение текущей даты
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),//получение и форматирование текущего времени
                transferId, //индентификатор перевода
                transfer.getCardFromNumber(),//номер карты перевода
                transfer.getCardToNumber(),//номер карты получателя
                transfer.getAmount().getValue(),//сумма перевода
                commission != null ? commission : "N/A",//коммисия за перевод
                status);//статус перевода
        logger.log(logMessage);//записывается сформированное сообщение в лог
    }
}

