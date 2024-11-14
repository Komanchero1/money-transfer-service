package org.example.moneytransferservice.advice;


import org.example.moneytransferservice.exception.ErrorTransferOrConfirmException;
import org.example.moneytransferservice.exception.InvalidDataException;
import org.example.moneytransferservice.logger.Logger;
import org.example.moneytransferservice.logger.LoggerImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicInteger;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger logger;
    private final AtomicInteger errorId;

    public ExceptionHandlerAdvice() {
        this.logger = LoggerImpl.getInstance();
        this.errorId = new AtomicInteger(0);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException e) {
        logger.log("Неверные данные: " + e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransferOrConfirmException.class)
    public ResponseEntity<ErrorResponse> handleTransferException(ErrorTransferOrConfirmException e) {
        logger.log("Ошибка перевода: " + e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class) // Обработка всех необработанных исключений
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        logger.log("Непредвиденная ошибка: " + e.getMessage());
        return buildResponse("Непредвиденная ошибка", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String msg, HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorResponse(msg, errorId.incrementAndGet()));
    }

}
