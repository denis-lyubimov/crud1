package com.example.crud1.config;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration // используоется как конфиг для теста в @SpringBootTest где ищется замена\добавление текущему конфигу, если класс пустой, то ошибки не будет
//@Configuration // если использовать конфигурейшн класс в @SpringBootTest, то весь конфигу будет заменен конфигом из этого класса, если класс пустой, то будет ошибка
public class TestConfig {
//    @Bean
//    public WebTestClient webClientBuilder(){
//        return new WebTestClient.Builder()
//                .baseUrl("http://localhost")
//                .defaultHeader("Accept", "application/json")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                .client
//                .build();
//    }
//    @Autowired
//    private PlayerController playerController;
//
//    @Bean
//    public WebTestClient getPlayerWebTestClient(){
//        return WebTestClient.bindToController(playerController).build();
//    }
}
