package org.example.moneytransferservice.exception;

//класс наследник RuntimeException который будет использоваться для обозначения ошибок,
// связанных с переводом или подтверждением операций
public class ErrorTransferOrConfirmException extends RuntimeException {

    //конструктор формирующий экземпляр ErrorTransferOrConfirmException содержащий сообщение msg об ошибке
    public ErrorTransferOrConfirmException(String msg) {
        super(msg);//вызов конструктора родительского класса RuntimeException с параметром msg
    }
}
