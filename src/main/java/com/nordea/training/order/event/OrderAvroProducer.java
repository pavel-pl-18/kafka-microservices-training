package com.nordea.training.order.event;

import com.nordea.training.avromodel.Order;
import com.nordea.training.model.OrderChange;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class OrderAvroProducer {

    public static final String TOPIC_NAME = "order";
    private final KafkaProducer<String, Order> kafkaProducer;

    public OrderAvroProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        kafkaProducer = new KafkaProducer(properties);
    }

    public void send(Order order) {
        ProducerRecord<String, Order> event =
                new ProducerRecord(TOPIC_NAME, order.getOrderId().toString(), order);

        try {
            kafkaProducer.send(event).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
