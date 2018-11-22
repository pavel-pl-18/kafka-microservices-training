package com.nordea.training.order.rest;

import com.nordea.training.order.Order;
import com.nordea.training.order.OrdersDatabase;
import com.nordea.training.order.event.OrderCreatedNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderRestController {

    @Autowired
    OrdersDatabase db;

    @Autowired
    OrderCreatedNotificationProducer orderCreatedNotificationProducer;

    @PostMapping
    public String createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        Order order = new Order();
        order.setProductId(orderCreateRequest.getProductId());
        order.setQuantity(orderCreateRequest.getQuantity());
        order.setUserId(orderCreateRequest.getUserId());
        order.setOrderId(UUID.randomUUID().toString());
        order.setCreatedAt(LocalDateTime.now());
        db.save(order);
        orderCreatedNotificationProducer.send(order);

        return order.getOrderId();
    }
}

/*
product id
quantity
user_id
 */