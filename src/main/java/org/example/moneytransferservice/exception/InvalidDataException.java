package org.example.moneytransferservice.exception;


//класс наследник RuntimeException который будет использоваться для обозначения ошибок, связанных с неверными данными
public class InvalidDataException extends RuntimeException {
    //конструктор формирующий экземпляр InvalidDataException содержащий сообщение msg об ошибке
    public InvalidDataException(String msg) {
        super(msg);//обращение к конструктору родительского класса
    }
}
