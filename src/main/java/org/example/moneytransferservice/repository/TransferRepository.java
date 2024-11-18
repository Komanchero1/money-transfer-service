package org.example.moneytransferservice.repository;

import org.example.moneytransferservice.logger.Logger;
import org.example.moneytransferservice.logger.LoggerImpl;
import org.example.moneytransferservice.model.Transfer;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.atomic.AtomicInteger;

@Repository // помечен как репозиторий и отвечает за управление операциями с переводами
public class TransferRepository {
    private final ConcurrentHashMap<String, TransferState> transferStateMap; //для хранения состояния переводов
    private final ConcurrentHashMap<String, Transfer> transferList; //для хранения объектов трансфер
    private final AtomicInteger id;//для генерации уникальных идентификаторов переводов, обеспечивает атомарные операции инкремента
    private final Logger logger; // для логирования

    public TransferRepository() {
        this.transferStateMap = new ConcurrentHashMap<>(); //инициализируется коллекция для хранения состояний переводов
        this.transferList = new ConcurrentHashMap<>(); //инициализируется коллекция для хранения объектов переводов
        this.id = new AtomicInteger(0); //инициализируется счетчик идентификаторов переводов с начальным значением 0
        this.logger = LoggerImpl.getInstance(); // Получаем экземпляр логгера
    }

    //метод для добавления перевода
    public String addTransfer(Transfer transfer) { //принимает объект Transfer в качестве аргумента
        String transferId = String.valueOf(id.incrementAndGet()); //генерируется уникальный индентификатор
        transferStateMap.putIfAbsent(transferId, TransferState.LOAD); //добавляется новое состояние перевода в коллекцию состояний
        transferList.put(transferId, transfer);//сохраняется объект перевода в коллекцию переводов с использованием сгенерированного идентификатора
        logger.log("Перевод добавлен: ID = " + transferId + ", с карты № " + transfer.getCardFromNumber() +
                ", на карту № " + transfer.getCardToNumber() + ", сумма " + transfer.getAmount().getValue()); //записывается сообщение о добавлении перевода в лог
        return transferId; //возвращает его идентификатор
    }

    //метод для подтверждения перевода по его индентификатору
    public Transfer confirmOperation(String id) {
        TransferState currentState = transferStateMap.get(id);//получение текущего состояния перевода по идентификатору
        if (currentState == null) {// если объекта с таким индентификатором нет
            logger.log("Не удалось подтвердить операцию ,перевод с данным № " + id + " не найден ");//логируется сообщение об ошибке
            return null; // возвращается null
        }
        // изменяется состояние на OK
        transferStateMap.put(id, TransferState.OK);
        logger.log("Операция подтверждена,ID №" + id); // логируется подтверждение операции
        return transferList.get(id);//возвращается объект Transfer, соответствующий идентификатору
    }


    //метод для получения состояния перевода  по его идентификатору
    public TransferState getTransferState(String id) {
        TransferState state = transferStateMap.get(id);//получается состояние перевода по идентификатору
        logger.log("перевод ID № " + id + ", статус  " + state); // логируется полученное состояние перевода, указывается идентификатор и текущее состояние
        return state; //возвращается состояние перевода
    }
}
