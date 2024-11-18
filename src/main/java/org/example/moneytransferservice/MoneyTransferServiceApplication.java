package org.example.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //указывает, что этот класс является основным классом конфигурации для приложения Spring Boot
//класс служит точкой входа в приложение
public class MoneyTransferServiceApplication {


    //метод для входа в приложение
    public static void main(String[] args) {
        //передается текущий класс в качестве аргумента, чтобы Spring знал,
        // какой класс использовать для настройки контекста приложения
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
    }
}
