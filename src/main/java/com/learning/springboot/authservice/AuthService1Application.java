package com.learning.springboot.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthService1Application {

    public static void main(String[] args) {
        SpringApplication.run(AuthService1Application.class, args);
    }

}
