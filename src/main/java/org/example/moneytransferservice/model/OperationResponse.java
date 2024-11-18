package org.example.moneytransferservice.model;

//класс представляет собой модель данных для ответа на операции
public class OperationResponse {

    private String operationId;//индентификатор операции

    //конструктор без параметров
    public OperationResponse() {}

    //создается объект OperationResponse содержащий индентификатор операции
    public OperationResponse(String operationId) {
        this.operationId = operationId;
    }

    @Override//переопределенный метод
    //для формирования строки содержащей индентификатор операции
    public String toString() {
        return "OperationResponse{" +
                "operationId='" + operationId + '\'' +
                '}';
    }

    //гетеры и сетеры

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
