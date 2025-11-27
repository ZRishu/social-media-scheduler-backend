package com.fierceadventurer.userservice.events;

import lombok.Data;

import java.util.UUID;

@Data
public class KeycloakUserCreatedEvent {
    private String userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
}

