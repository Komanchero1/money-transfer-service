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


@RestControllerAdvice //аннотация,объединяет функциональность @ControllerAdvice и @ResponseBody
//класс для обработки исключенияй, возникающих в контроллерах, и возвращения структурированного ответы в формате JSON
public class ExceptionHandlerAdvice {
    private final Logger logger;//для хранения экземпляра Logger, который используется для записи сообщений об ошибках
    private final AtomicInteger errorId;//для хранения уникального индентификатора ошибок


    //инициализируем поля класса в конструкторе
    public ExceptionHandlerAdvice() {
        this.logger = LoggerImpl.getInstance();
        this.errorId = new AtomicInteger(0);
    }




    //аннотация указывающая то этот метод будет обрабатывать исключения типа InvalidDataException
    @ExceptionHandler(InvalidDataException.class)
    //метод для обработки исключений invalidDataException
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException e) {
        logger.log("Неверные данные: " + e.getMessage());//записывается в лог файл
        //возвращение сообщения об ошибке и статуса HTTP 400 (Bad Request)
        return buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //аннотация указывает, что тот метод будет обрабатывать исключения типа ErrorTransferOrConfirmException
    @ExceptionHandler(ErrorTransferOrConfirmException.class)
    //метод для обработки исключения ErrorTransferOrConfirmException
    public ResponseEntity<ErrorResponse> handleTransferException(ErrorTransferOrConfirmException e) {
        logger.log("Ошибка перевода: " + e.getMessage());//записывается в лог файл
        //Возвращение сообщения об ошибке и статусом HTTP 500 (Internal Server Error)
        return buildResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class) // аннотация указывает что этот метод будет обрабатывать все не обработанные исключения
    //метод обрабатывающий общее исключение
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        logger.log("Некорректные данные: " + e.getMessage());//записывается в лог файл
        //возвращается сообщение с общим сообщением об ошибке и статусом HTTP 500 (Internal Server Error)
        return buildResponse("Некорректные данные", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //метод для формирования ответа об ошибке
    private ResponseEntity<ErrorResponse> buildResponse(String msg, HttpStatus status) {
        //возвращение объекта ResponseEntity который содержит экземпляр ErrorResponse с сообщением об ошибке
        return ResponseEntity.status(status).body(new ErrorResponse(msg, errorId.incrementAndGet()));
    }
}
