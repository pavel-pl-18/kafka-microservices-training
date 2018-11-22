package com.nordea.training.order;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrdersDatabase {
    ConcurrentHashMap<String, Order> db = new ConcurrentHashMap();

    public void save(Order order) {
        db.put(order.getOrderId(), order);
    }
}
