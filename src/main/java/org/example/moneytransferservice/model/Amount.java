package org.example.moneytransferservice.model;


public class Amount {

    private Integer value;

    public Amount() {}

    public Amount(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
