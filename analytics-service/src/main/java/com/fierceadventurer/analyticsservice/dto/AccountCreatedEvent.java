package com.fierceadventurer.analyticsservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountCreatedEvent {
    private UUID socialAccountId;
    private String provider;
}
