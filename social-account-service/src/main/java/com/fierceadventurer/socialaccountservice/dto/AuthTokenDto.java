package com.fierceadventurer.socialaccountservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class AuthTokenDto {
    private UUID tokenId;

    private LocalDateTime expiry;
    private List<String> scopes;
}
