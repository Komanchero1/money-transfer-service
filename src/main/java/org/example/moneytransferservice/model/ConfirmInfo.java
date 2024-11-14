package org.example.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;

public class ConfirmInfo {


    private String operationId;

    @NotBlank(message = "Код не может быть пустым")
    private String code;

    public ConfirmInfo() {}

    public ConfirmInfo(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

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

    @Override
    public String toString() {
        return "ConfirmInfo{" +
                "operationId='" + operationId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
