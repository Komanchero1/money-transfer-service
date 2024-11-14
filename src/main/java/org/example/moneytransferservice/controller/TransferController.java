package org.example.moneytransferservice.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.example.moneytransferservice.model.ConfirmInfo;
import org.example.moneytransferservice.model.OperationResponse;
import org.example.moneytransferservice.model.Transfer;
import org.example.moneytransferservice.repository.TransferState;
import org.example.moneytransferservice.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;
    private Logger logger;


    public TransferController(TransferService transferService) {
        this.transferService = transferService;

    }

    @PostMapping
    public ResponseEntity<OperationResponse> transferMoney(@Valid @RequestBody Transfer transfer) {
        try {
            String transferId = transferService.initiateTransfer(transfer);
            return ResponseEntity.ok(new OperationResponse(transferId));
        } catch (Exception e) {
            // Логируем ошибку
            logger.error("Ошибка при обработке запроса: ", e);
            return ResponseEntity.badRequest().body(new OperationResponse("Ошибка: " + e.getMessage()));
        }
    }


    @PostMapping("/confirmOperation")
    public ResponseEntity<OperationResponse> confirmOperation(@Valid@RequestBody ConfirmInfo confirmInfo) {
        Transfer transfer = transferService.confirmTransfer(confirmInfo.getOperationId());
        if (transfer != null) {
            return ResponseEntity.ok(new OperationResponse(confirmInfo.getOperationId()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OperationResponse("Неверный идентификатор операции"));
        }
    }

    @GetMapping("/state/{id}")
    public ResponseEntity<TransferState> getTransferState(@Valid@PathVariable String id) {
        TransferState state = transferService.getTransferState(id);
        if (state != null) {
            return ResponseEntity.ok(state);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
