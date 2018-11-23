package com.nordea.training.user;

import org.springframework.boot.SpringApplication;

public class UserService {
    public static void main(String[] args) {
        System.setProperty("server.port", "9000");
        SpringApplication.run(UserService.class, args);

    }
}
