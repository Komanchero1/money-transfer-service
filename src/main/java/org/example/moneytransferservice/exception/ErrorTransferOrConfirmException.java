package org.example.moneytransferservice.exception;

public class ErrorTransferOrConfirmException extends RuntimeException {
    public ErrorTransferOrConfirmException(String msg) {
        super(msg);
    }
}
