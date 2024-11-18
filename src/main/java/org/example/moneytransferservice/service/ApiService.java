package org.example.moneytransferservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service//помечаем класс как сервис
//класс который будет использоваться для взаимодействия с внешним RESTful приложением
public class ApiService {
    private final RestTemplate restTemplate;//поле использоваться для выполнения HTTP-запросов


    //конструктор для создания объекта ApiService обращающийся к классу RestTemplate для выполнения
    // HTTP-запросов и преобразовывающий их в объекты джава
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    //метод для выполнения HTTP-запросов
    public String getDataFromRestFront() {
        String url = "https://serp-ya.github.io/card-transfer/"; // URL на который отправится запрос
        return restTemplate.getForObject(url, String.class);//возвращение ответа в виде строки
    }
}
