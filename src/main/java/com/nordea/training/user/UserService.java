package com.nordea.training.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserService {
    public static void main(String[] args) {
        System.setProperty("server.port", "9000");
        SpringApplication.run(UserService.class, args);

    }
}
