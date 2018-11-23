package com.nordea.training.user;

import com.nordea.training.avromodel.User;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@RestController("/user")
public class UserAPI {
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String TOPIC_NAME = "user";
    private final KafkaProducer kafkaProducer;

    public UserAPI() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "user-service");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                "http://localhost:8081");
        kafkaProducer = new KafkaProducer(properties);
    }

    @PostMapping
    public void updateCustomer(@RequestBody UserData data) {
        User avro = new User();
        avro.setAddress(data.getAddress());
        avro.setUserId(data.getId());
        avro.setName(data.getName());

        ProducerRecord producerRecord =
                new ProducerRecord(TOPIC_NAME, avro.getUserId(), avro);

        try {
            kafkaProducer.send(producerRecord).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
