package com.fierceadventurer.socialaccountservice.service.Impl;

import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import com.fierceadventurer.socialaccountservice.exception.ResourceNotFoundException;
import com.fierceadventurer.socialaccountservice.mapper.SocialAccountMapper;
import com.fierceadventurer.socialaccountservice.repository.SocialAccountRepository;
import com.fierceadventurer.socialaccountservice.service.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryServiceImpl implements AccountQueryService {

    private final SocialAccountRepository socialAccountRepository;
    private final SocialAccountMapper  socialAccountMapper;

    @Override
    public Page<SocialAccountResponseDto> getAccountsByUserId(UUID userId, Pageable pageable) {
        return socialAccountRepository.findByUserId(userId, pageable)
                .map(socialAccountMapper::toDto);
    }

    @Override
    public SocialAccountResponseDto getAccountById(UUID accountId) {
        return socialAccountRepository.findById(accountId)
                .map(socialAccountMapper::toDto).orElseThrow(
                        ()-> new ResourceNotFoundException("Social account not found with id: " + accountId)
        );
    }
}
