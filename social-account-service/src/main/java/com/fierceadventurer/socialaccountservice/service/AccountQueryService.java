package com.fierceadventurer.socialaccountservice.service;

import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AccountQueryService {
    Page<SocialAccountResponseDto> getAccountsByUserId(UUID userId , Pageable pageable);
    SocialAccountResponseDto getAccountById(UUID accountId);
}
