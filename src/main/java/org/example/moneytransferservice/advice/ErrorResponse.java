package org.example.moneytransferservice.advice;

public class ErrorResponse {
    private String message;
    private int id;

    public ErrorResponse(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
