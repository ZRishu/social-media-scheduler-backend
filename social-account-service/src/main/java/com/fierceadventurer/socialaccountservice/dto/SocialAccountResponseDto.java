package com.fierceadventurer.socialaccountservice.dto;

import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import com.fierceadventurer.socialaccountservice.enums.AccountStatus;
import com.fierceadventurer.socialaccountservice.enums.Provider;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SocialAccountResponseDto {

    private UUID accountId;
    private UUID userId;
    private Provider provider;
    private String username;
    private String displayName;
    private String profileImageUrl;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<AuthTokenDto> authToken;
    private RateLimitQuotaDto rateLimitQuota;
}
