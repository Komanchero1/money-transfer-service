package org.example.moneytransferservice.model;

//класс использоваться для передачи данных о суммах в других частях приложения
public class Amount {

    private Integer value;//поле для хранения значения суммы

    //конструктор без аргументов
    public Amount() {}

    //онструктор позволяет создавать экземпляры класса Amount с заданным значением
    public Amount(Integer value) {
        this.value = value;
    }

    //гетеры и сетеры

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
