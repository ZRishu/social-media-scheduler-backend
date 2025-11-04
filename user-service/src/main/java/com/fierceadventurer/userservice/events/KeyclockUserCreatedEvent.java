package com.fierceadventurer.userservice.events;

import lombok.Data;

import java.util.UUID;

@Data
public class KeyclockUserCreatedEvent {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
}

