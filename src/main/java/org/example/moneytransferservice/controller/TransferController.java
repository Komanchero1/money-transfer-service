package org.example.moneytransferservice.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.example.moneytransferservice.logger.LoggerImpl;
import org.example.moneytransferservice.model.ConfirmInfo;
import org.example.moneytransferservice.model.OperationResponse;
import org.example.moneytransferservice.model.Transfer;
import org.example.moneytransferservice.repository.TransferState;
import org.example.moneytransferservice.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //помечается класс как контроллер
@RequestMapping("/transfer")//все запросы к этому контроллеру будут начинаться /transfer
public class TransferController {
    //поле для взаимодействия с логикой перевода ,поле должно быть обязательно
    // инициализировано в конструкторе
    private final TransferService transferService;
    private  Logger logger;//поле для логирования

    //конструктор, принимает в качестве аргумента объект transferService
    public TransferController(TransferService transferService) {
        this.transferService = transferService;

    }

    //метод для перевода денег
    @PostMapping //метод будет обрабатывать POST запросы по URL /transfer
    // качестве параметра метод принимает десериализованый  объект transfer и проверенный
    // на соответствие правилам валидации определенным в классе Transfer
    public ResponseEntity<OperationResponse> transferMoney(@Valid @RequestBody Transfer transfer) {
        try {
            String transferId = transferService.initiateTransfer(transfer);// метод transferService инициирует перевод и возвращает идентификатор перевода
            return ResponseEntity.ok(new OperationResponse(transferId));//если перевод успешен, возвращается ответ с кодом 200 (OK) и идентификатором операции
        } catch (Exception e) {
            // если возникает ошибка, она логируется
            logger.error("Ошибка при обработке запроса: ", e);
            //возвращается ответ с кодом 400 (Bad Request) и сообщением об ошибке
            return ResponseEntity.badRequest().body(new OperationResponse("Ошибка: " + e.getMessage()));
        }
    }

    //метод для подтверждения операции
    @PostMapping("/confirmOperation") //метод обрабатывает POST-запросы по URL /transfer/confirmOperation
    //в качестве параметра принимает объект confirmInfo содержащий информацию о подтверждении операции
    public ResponseEntity<OperationResponse> confirmOperation(@Valid@RequestBody ConfirmInfo confirmInfo) {

        //вызывается метод initiateTransfer который инициирует перевод и возвращает идентификатор перевода
        Transfer transfer = transferService.confirmTransfer(confirmInfo.getOperationId());
        if (transfer != null) { //если объект найден
            return ResponseEntity.ok(new OperationResponse(confirmInfo.getOperationId()));//возвращается ответ с кодом 200 (OK) и идентификатором операции
        } else {
            // Логируется ошибкуао неверного идентификатора операции
            logger.error("Неверный идентификатор операции: {}", confirmInfo.getOperationId());
            //возвращается ответ с кодом 400 и сообщением о неверном идентификаторе
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OperationResponse("Неверный идентификатор операции"));
        }
    }

    //метод для получения состояния перевода
    @GetMapping("/state/{id}") //метод обрабатывает GET-запросы по URL /transfer/state/{id}
    //@PathVariable используется для извлечения значения идентификатора из URL
    public ResponseEntity<TransferState> getTransferState(@Valid@PathVariable String id) {
        //вызывает getTransferState, чтобы получить состояние перевода по идентификатору
        TransferState state = transferService.getTransferState(id);
        if (state != null) { //если состояние найдено
            return ResponseEntity.ok(state); //возвращается ответ с кодом 200 (OK) и состоянием
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //возвращается ответ с кодом 404 без тела
        }
    }
}
