package com.example.contactApp.kafka_consumer;

import com.example.contactApp.model.Group;
import com.example.contactApp.repository.GroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final GroupRepository groupRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(GroupRepository groupRepository, ObjectMapper objectMapper) {
        this.groupRepository = groupRepository;
        this.objectMapper = objectMapper;
        logger.info("KafkaConsumer class loaded and KafkaConsumer bean created.");
    }

    @KafkaListener(topics = "new-group-topic", groupId = "group-group")
    public void consume(String message) {
        logger.info("Received Kafka message: {}", message);
        try {
            Group group = objectMapper.readValue(message, Group.class);
            processGroup(group);
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }

    private void processGroup(Group group) {
        // Log incoming group details for debugging
        logger.info("Processing group: {}", group);

        // Check if a group with the same name already exists
        Group existingGroup = groupRepository.findByName(group.getName()).orElse(null);

        if (existingGroup == null) {
            // If the group does not exist, save it
            Group savedGroup = groupRepository.save(group);
            logger.info("Processed and saved new group: {}", savedGroup);
        } else {
            // Log the details of the existing group
            logger.info("Group already exists and was not saved: {}", existingGroup);
        }
    }
}
