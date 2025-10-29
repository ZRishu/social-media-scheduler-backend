package com.fierceadventurer.socialaccountservice.service;

import com.fierceadventurer.socialaccountservice.dto.CreateSocialAccountRequestDto;
import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;

import java.util.UUID;

public interface SocialAccountService {
    SocialAccountResponseDto createSocialAccount(CreateSocialAccountRequestDto requestDto);
    void suspendAccount(UUID accountId, String reason);
    void unsuspendAccount(UUID accountId);
    void deleteSocialAccount(UUID accountId);
    void markedAccountTokenExpired(UUID accountId);
    String getActiveAccessToken(UUID accountId);
}
