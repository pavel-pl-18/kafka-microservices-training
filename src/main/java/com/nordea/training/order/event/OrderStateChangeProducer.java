package com.nordea.training.order.event;

import com.nordea.training.model.Order;
import com.nordea.training.model.OrderChange;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class OrderStateChangeProducer {

    public static final String TOPIC_NAME = "order-changed";
    private final KafkaProducer<String, OrderChange> kafkaProducer;

    public OrderStateChangeProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderChangeJSONSerializer.class.getName());

        kafkaProducer = new KafkaProducer(properties);
    }

    public void send(OrderChange orderChange) {
        ProducerRecord<String, OrderChange> event =
                new ProducerRecord(TOPIC_NAME, orderChange.getCurrent().getOrderId(), orderChange);

        try {
            kafkaProducer.send(event).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
