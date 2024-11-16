package org.example.moneytransferservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getDataFromRestFront() {
        String url = "https://serp-ya.github.io/card-transfer/api"; // нужный URL
        return restTemplate.getForObject(url, String.class);
    }
}
