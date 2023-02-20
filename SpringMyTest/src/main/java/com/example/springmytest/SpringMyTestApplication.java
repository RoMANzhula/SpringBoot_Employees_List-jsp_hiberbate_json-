package com.example.springmytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMyTestApplication {
    public static void main(String[] args) {

        SpringApplication.run(SpringMyTestApplication.class, args); //Beans (бины - т.е. обьекты классов, которыми управляет
        // Spring (репозитории, сервисы, контроллеры)) инициализируются после данного метода run()
    }

}
