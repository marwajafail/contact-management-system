package com.example.contactApp.kafka_producer;

import com.example.contactApp.model.Group;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    // Logger for logging information and errors
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    // Kafka topic to which the new group details will be sent
    private static final String NEW_GROUP_TOPIC = "new-group-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    // ObjectMapper for converting Group objects to JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendNewGroupDetails(Group group) {
        try {
            // Convert the Group object to a JSON string
            String message = objectMapper.writeValueAsString(group);
            // Send the JSON message to the Kafka topic
            kafkaTemplate.send(NEW_GROUP_TOPIC, message);
            // Log the successful sending of the message
            logger.info("New group details sent: {}", message);
        } catch (JsonProcessingException e) {
            // Log an error if serialization fails
            logger.error("Failed to serialize group details: {}", group, e);
        }
    }
}
