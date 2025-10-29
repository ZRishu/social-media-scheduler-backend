package com.fierceadventurer.socialaccountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent {
    private UUID socialAccountId;
    private String provider;
}
