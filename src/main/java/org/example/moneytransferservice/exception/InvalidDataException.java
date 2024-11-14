package org.example.moneytransferservice.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String msg) {
        super(msg);
    }
}
