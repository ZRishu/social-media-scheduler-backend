package com.fierceadventurer.socialaccountservice.service;

import com.fierceadventurer.socialaccountservice.dto.CreateSocialAccountRequestDto;
import com.fierceadventurer.socialaccountservice.dto.PublishRequestDto;
import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SocialAccountService {
    SocialAccountResponseDto createSocialAccount(UUID userId ,CreateSocialAccountRequestDto requestDto);
    void suspendAccount(UUID accountId, String reason);
    void unsuspendAccount(UUID accountId);
    void deleteSocialAccount(UUID accountId);
    void markedAccountTokenExpired(UUID accountId);
    String getActiveAccessToken(UUID accountId);
    void validateAccountOwnership(UUID accountId, UUID userId);
    Page<SocialAccountResponseDto> getAccountsByUserId(UUID userId, Pageable pageable);
    void syncAccountFromKeycloak(UUID userId, String userJwtToken);
    String publishContent(UUID accountId, PublishRequestDto requestDto);

}
