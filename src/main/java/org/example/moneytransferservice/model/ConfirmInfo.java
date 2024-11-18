package org.example.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;

//класс для хранения информации, необходимой для подтверждения операции
public class ConfirmInfo {


    private String operationId;//поле для хранения индентификатора операции

    @NotBlank(message = "Код не может быть пустым")//аннотация валидации, указывающая, что поле code не должно быть пустым
    private String code;//поле для хранения кода операции

    //конструктор без параметров
    public ConfirmInfo() {}

    //конструктор для создания объекта ConfirmInfo с заданными значениями
    public ConfirmInfo(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    @Override//переопределенный метод
    //метод для формирования строки, которая включает значения полей operationId и code
    public String toString() {
        return "ConfirmInfo{" +
                "operationId='" + operationId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    //гетеры и сетеры

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
