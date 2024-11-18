package org.example.moneytransferservice.advice;


//класс для хранения информации об ошибках
public class ErrorResponse {
    private String message;//для хранения текстового сообщения об ошибке
    private int id;//для хранения уникального индентификатора ошибки

    //конструктор создающий объект содержащий сообщение об ошибки и уникальный индентификатор ошибки
    public ErrorResponse(String message, int id) {
        this.message = message;
        this.id = id;
    }

    //гетеры и сетеры

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
