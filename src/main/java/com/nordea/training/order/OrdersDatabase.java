package com.nordea.training.order;

import com.nordea.training.model.Order;
import com.nordea.training.model.OrderChange;
import com.nordea.training.order.event.OrderStateChangeProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrdersDatabase {
    ConcurrentHashMap<String, Order> db = new ConcurrentHashMap();

    @Autowired
    OrderStateChangeProducer orderStateChangeProducer;

    public void save(Order order) {
        Order oldOrder = db.getOrDefault(order.getOrderId(), null);
        db.put(order.getOrderId(), order);

        OrderChange orderChange = new OrderChange();
        orderChange.setTs(LocalDateTime.now());
        orderChange.setOld(oldOrder);
        orderChange.setCurrent(order);
        orderStateChangeProducer.send(orderChange);
    }

    public Order getById(String id) {
        return db.getOrDefault(id, null);
    }
}
