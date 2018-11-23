package com.nordea.training.shipping;

import com.nordea.training.avromodel.Order;
import com.nordea.training.avromodel.OrderStatus;
import com.nordea.training.avromodel.User;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Collections;
import java.util.Properties;

public class ShippingStreams {

    public static void main(String[] args) {
        Properties streamProperties = new Properties();
        streamProperties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, "shipping-service-streams");
        streamProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamProperties.put(StreamsConfig.EXACTLY_ONCE, "true");
        streamProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        streamProperties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        streamProperties.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/state_dir");
        streamProperties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "5");

        Serde<User> userSerde = new SpecificAvroSerde();
        userSerde.configure(Collections.singletonMap("schema.registry.url", "http://localhost:8081"), false);
        Serde<Order> orderSerde = new SpecificAvroSerde();
        orderSerde.configure(Collections.singletonMap("schema.registry.url", "http://localhost:8081"), false);

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        GlobalKTable<Long, User> users = streamsBuilder.globalTable("user", Consumed.with(Serdes.Long(), userSerde));

        KStream<String, ShippingLabel> shippingLabelsStream = streamsBuilder
                .stream("order", Consumed.with(Serdes.String(), orderSerde))
                .filter((orderId, order) -> order.getStatus() == OrderStatus.CREATED)
                .join(users,
                        (orderId, order) -> order.getUserId(),
                        (order, user) -> new ShippingLabel(user, order));

        shippingLabelsStream
                .mapValues(shippingLabel -> "Order with product " + shippingLabel.getOrder().getProductId() + " will be shipped to " + shippingLabel.getUser().getAddress())
                .to("shipping-label", Produced.with(Serdes.String(), Serdes.String()));

        shippingLabelsStream
                .mapValues(shippingLabel -> "Order with product " + shippingLabel.getOrder().getProductId() + " will be shipped to " + shippingLabel.getUser().getAddress())
                .to("shipping-label-2", Produced.with(Serdes.String(), Serdes.String()));

        Topology topology = streamsBuilder.build();

        KafkaStreams kafkaStreams = new KafkaStreams(topology, streamProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

    }
}

