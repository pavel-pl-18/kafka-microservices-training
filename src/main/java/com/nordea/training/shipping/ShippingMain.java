package com.nordea.training.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Handler;
import java.util.logging.Logger;

@SpringBootApplication
public class ShippingMain {
    public static void main(String[] args) {
        SpringApplication.run(ShippingMain.class, "--spring.main.web-environment=false");
    }
}

/*
ConsumerRecord(topic = order-created,
partition = 0,
offset = 0,
CreateTime = 1542961567390,
serialized key size = 36, serialized value size = 54,
headers = RecordHeaders(headers = [], isReadOnly = false),
key = ce4f4ae2-24ef-41f1-9738-6f62abd7298d,
value = Order ce4f4ae2-24ef-41f1-9738-6f62abd7298d was created)

 */