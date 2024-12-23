package com.example.springmytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMyTestApplication {
    public static void main(String[] args) {

       SpringApplication.run(SpringMyTestApplication.class, args); //Біни (об'єкти класів, якими керує
        //Spring (репозиторії, сервіси, контролери)) ініціалізуються після виклику цього методу run()

    }

}
