package com.nordea.training.shipping;

import com.nordea.training.model.OrderChange;
import com.nordea.training.model.OrderStatus;
import com.nordea.training.order.event.OrderCreatedNotificationProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class OrderCreatedNotificationConsumer implements ApplicationRunner {

    private final KafkaConsumer<String, OrderChange> consumer;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public OrderCreatedNotificationConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderChangeDeserializer.class.getName());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "shipping-service-2");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        consumer = new KafkaConsumer(properties);
        consumer.subscribe(Collections.singletonList("order-changed"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            ConsumerRecords<String, OrderChange> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, OrderChange> record : records) {
                processOrderChange(record.value());

                System.out.println(record.value());
                Thread.sleep(10000);
                // processing

            }
            consumer.commitSync();

        }
    }

    private void processOrderChange(OrderChange change) throws InterruptedException, ExecutionException {
        if (change.getOld() == null && change.getCurrent().getStatus() == OrderStatus.CREATED) {
            // generate label
            Thread.sleep(5000);
            kafkaTemplate.send("shipping-label-generated",
                    change.getCurrent().getOrderId(),
                    change.getCurrent().getOrderId()).get();

        }
    }
}
