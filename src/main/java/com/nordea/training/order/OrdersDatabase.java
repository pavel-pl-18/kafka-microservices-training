package com.nordea.training.order;

import com.nordea.training.model.Order;
import com.nordea.training.model.OrderChange;
import com.nordea.training.order.event.OrderAvroProducer;
import com.nordea.training.order.event.OrderStateChangeProducer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrdersDatabase {
    ConcurrentHashMap<String, Order> db = new ConcurrentHashMap();

    @Autowired
    OrderStateChangeProducer orderStateChangeProducer;

    @Autowired
    OrderAvroProducer orderAvroProducer;

    public void save(Order order) {
        Order oldOrder = db.getOrDefault(order.getOrderId(), null);
        db.put(order.getOrderId(), order);

        OrderChange orderChange = new OrderChange();
        orderChange.setTs(LocalDateTime.now());
        orderChange.setOld(oldOrder);
        orderChange.setCurrent(order);
        orderStateChangeProducer.send(orderChange);
        sendAvro(order);
    }

    private void sendAvro(Order order) {
        com.nordea.training.avromodel.Order orderAvro = new com.nordea.training.avromodel.Order();
        orderAvro.setOrderId(order.getOrderId());
        orderAvro.setProductId(order.getProductId());
        orderAvro.setQuantity(order.getQuantity());
        orderAvro.setStatus(com.nordea.training.avromodel.OrderStatus.valueOf(order.getStatus().toString()));
        orderAvro.setUserId(order.getUserId());
        orderAvro.setCreatedAt(new DateTime(order.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        orderAvro.setAsGift(true);
        orderAvroProducer.send(orderAvro);
    }

    public Order getById(String id) {
        return db.getOrDefault(id, null);
    }
}
