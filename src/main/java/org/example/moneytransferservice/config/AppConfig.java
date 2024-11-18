package org.example.moneytransferservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration //помечаем что класс является классовой конфмгурацией Spring
//класс для определения бинов, которые будут созданы и управляются Spring
public class AppConfig {


    @Bean //указывает что метод будет возвращать объект который должен быть зарегестрирован в контейнере как бин
    //метод для создания бина
    public RestTemplate restTemplate() {
        return new RestTemplate();//создается объект RestTemplate который можно использовать как бин в других компонентах приложения
    }
}
