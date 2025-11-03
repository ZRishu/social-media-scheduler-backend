package com.fierceadventurer.userservice.service;

import com.fierceadventurer.userservice.entity.User;
import com.fierceadventurer.userservice.enums.UserStatus;
import com.fierceadventurer.userservice.events.KeyClockUserCreatedEvent;
import com.fierceadventurer.userservice.mapper.UserMapper;
import com.fierceadventurer.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyClockEventListener {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @KafkaListener(topics = "keyClock.user.created", groupId = "user-service-group")
    @Transactional
    public void handleUserCreation(KeyClockUserCreatedEvent event){
        log.info("Received new user creation event for userId: {}", event.getUserId());

        if(userRepository.existsById(event.getUserId())){
            log.warn("User profile for {} already exists. Skipping creation", event.getUserId());
            return;
        }

        User user = userMapper.toEntity(event);

        String displayName = event.getFirstName();
        if(displayName == null || displayName.isBlank()){
            displayName = event.getEmail().split("@")[0];
        }
        user.setDisplayName(displayName);

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("Successfully created user profile for {}", event.getEmail());
    }
}
