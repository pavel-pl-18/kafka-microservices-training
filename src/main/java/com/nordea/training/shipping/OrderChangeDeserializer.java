package com.nordea.training.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nordea.training.model.OrderChange;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class OrderChangeDeserializer implements Deserializer<OrderChange> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public OrderChange deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(data, OrderChange.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {

    }
}
