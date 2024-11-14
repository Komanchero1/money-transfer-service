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

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final Logger logger;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
        this.logger = LoggerImpl.getInstance();
    }

    public String initiateTransfer(Transfer transfer) {
        String transferId = transferRepository.addTransfer(transfer);
        logTransfer(transfer, transferId, " Инициализированно ", null);
        return transferId;
    }

    public Transfer confirmTransfer(String transferId) {
        Transfer transfer = transferRepository.confirmOperation(transferId);
        if (transfer == null) {
            logger.log(" ID " + transferId + " не найден для подтверждения.");
            throw new InvalidDataException("не найден ID: " + transferId);
        }
        logTransfer(transfer, transferId, "Подтвержденный", null);
        return transfer;
    }


    public TransferState getTransferState(String transferId) {
        return transferRepository.getTransferState(transferId);
    }

    private void logTransfer(Transfer transfer, String transferId, String status, Double commission) {
        String logMessage = String.format("Date: %s, Time: %s, Transfer ID: %s, From Card: %s, To Card: %s, Amount: %d, Commission: %s, Status: %s",
                LocalDate.now(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                transferId,
                transfer.getCardFromNumber(),
                transfer.getCardToNumber(),
                transfer.getAmount().getValue(),
                commission != null ? commission : "N/A",
                status);
        logger.log(logMessage);
    }
}

