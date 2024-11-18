package org.example.moneytransferservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//класс модель данных для перевода денег
public class Transfer {


    //аннотация указывающая что поле не может быть пустым, в противном случае будет выдано сообщение об ошибке
    @NotBlank(message = "Номер карты не может быть пустой")
    //аннотация указывающая что номер карты должен содержать 16 цифр, в противном случае будет выдано сообщение об ошибке
    @Size(min = 16, max = 16, message = "Номер карты должен состоять из 16 цифр")
    private String cardFromNumber;//переменная для хранения номера карты с которой переводят деньги

    @NotBlank(message = "Срок действия карты не может быть пустым")
    @Size(min = 5, max = 5, message = "Дата срока действия карты должна соответствовать в формату MM/YY")
    private String cardFromValidTill;//для хранения срока действия карты отправителя

    @NotBlank(message = "CVV не может быть пустым")
    @Size(min = 3, max = 3, message = "CVV должен состоять из 3 цифр")
    private String cardFromCVV;//для хранения кода безопасности карты отправителя

    @NotBlank(message = "Номер карты зачисления не может быть пустым")
    @Size(min = 16, max = 16, message = "Номер карты зачисления должен состоять из 16 цифр")
    private String cardToNumber;//для хранения номера карты получателя


    //аннотация валидации, указывающая, что поле amount не должно быть null,в противном случае будет выдано сообщение об ошибке
    @NotNull(message = "Сумма перевода должна быть больше ноля")
    private Amount amount; //для хранения объекта Amount, который представляет собой сумму перевода

    // геттеры и сеттеры

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public void setCardFromNumber(String cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public void setCardFromValidTill(String cardFromValidTill) {
        this.cardFromValidTill = cardFromValidTill;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public void setCardFromCVV(String cardFromCVV) {
        this.cardFromCVV = cardFromCVV;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public void setCardToNumber(String cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
