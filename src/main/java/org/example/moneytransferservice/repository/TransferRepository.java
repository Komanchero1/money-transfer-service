package org.example.moneytransferservice.repository;

import org.example.moneytransferservice.logger.Logger;
import org.example.moneytransferservice.logger.LoggerImpl;
import org.example.moneytransferservice.model.Transfer;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TransferRepository {
    private final ConcurrentHashMap<String, TransferState> transferStateMap;
    private final ConcurrentHashMap<String, Transfer> transferList;
    private final AtomicInteger id;
    private final Logger logger; // Логгер для логирования

    public TransferRepository() {
        this.transferStateMap = new ConcurrentHashMap<>();
        this.transferList = new ConcurrentHashMap<>();
        this.id = new AtomicInteger(0);
        this.logger = LoggerImpl.getInstance(); // Получаем экземпляр логгера
    }

    public String addTransfer(Transfer transfer) {
        String transferId = String.valueOf(id.incrementAndGet());
        transferStateMap.putIfAbsent(transferId, TransferState.LOAD);
        transferList.put(transferId, transfer);
        logger.log("Перевод добавлен: ID = " + transferId + ", с карты № " + transfer.getCardFromNumber() +
                ", на карту № " + transfer.getCardToNumber() + ", сумма " + transfer.getAmount().getValue());
        return transferId;
    }


    public Transfer confirmOperation(String id) {
        TransferState currentState = transferStateMap.get(id);
        if (currentState == null) {
            logger.log("Не удалось подтвердить операцию ,перевод с данным № " + id + " не найден ");
            return null; // Не существует перевода с таким ID
        }
        // Изменяем состояние на OK
        transferStateMap.put(id, TransferState.OK);
        logger.log("Операция подтверждена,ID №" + id); // Логируем подтверждение
        return transferList.get(id);
    }



    public TransferState getTransferState(String id) {
        TransferState state = transferStateMap.get(id);
        logger.log("перевод ID № " + id + ", статус  " + state); // Логируем получение состояния
        return state;
    }
}
