package com.nordea.training.order;

import com.nordea.training.model.Order;
import com.nordea.training.model.OrderStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ShippingLabelNotificationConsumer {

    @Autowired
    OrdersDatabase ordersDatabase;

    @KafkaListener(topics = "shipping-label-generated")
    public void listen(ConsumerRecord<String, String> labelGeneratedNotification) throws Exception {
        try {
            Order current = ordersDatabase.getById(labelGeneratedNotification.value());
            if (current == null) {
                System.out.println("Order not in DB: " + labelGeneratedNotification.value());
                return;
            }

            current = current.clone();
            current.setStatus(OrderStatus.PREPARED_TO_SHIPPING);
            ordersDatabase.save(current);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
