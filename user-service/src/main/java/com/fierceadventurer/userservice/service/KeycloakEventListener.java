package com.fierceadventurer.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.userservice.entity.User;
import com.fierceadventurer.userservice.enums.UserStatus;
import com.fierceadventurer.userservice.events.KeycloakUserCreatedEvent;
import com.fierceadventurer.userservice.mapper.UserMapper;
import com.fierceadventurer.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakEventListener {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "keycloak.user.created", groupId = "user-service-sync-group")
    @Transactional
    public void handleUserCreation(String message){
        log.info("Received raw kafka message: {} " , message);

        try {
            KeycloakUserCreatedEvent event = objectMapper.readValue(message, KeycloakUserCreatedEvent.class);

            log.info("Processing user creation for userId: {}", event.getUserId());

            if (userRepository.existsByEmail(event.getEmail())) {
                log.warn("User profile for {} already exists. Skipping creation", event.getEmail());
                return;
            }

            User user = userMapper.toEntity(event);

            String displayName = event.getFirstName();
            if (displayName == null || displayName.isBlank()) {
                displayName = event.getEmail().split("@")[0];
            }
            user.setDisplayName(displayName);

            user.setStatus(UserStatus.ACTIVE);
            user.setId(UUID.fromString(event.getUserId()));
            userRepository.save(user);
            log.info("Successfully created user profile for {}", event.getEmail());
        }
        catch (JsonProcessingException e){
            log.error("Failed to parse Keycloak event JSON", e);
        }
        catch (Exception e){
            log.error("Error saving user to database", e);
        }
    }
}
