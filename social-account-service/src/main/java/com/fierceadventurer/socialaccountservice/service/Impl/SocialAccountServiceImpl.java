package com.fierceadventurer.socialaccountservice.service.Impl;

import com.fierceadventurer.socialaccountservice.config.RateLimitProperties;
import com.fierceadventurer.socialaccountservice.dto.CreateSocialAccountRequestDto;
import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import com.fierceadventurer.socialaccountservice.entities.AuthToken;
import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import com.fierceadventurer.socialaccountservice.enums.AccountStatus;
import com.fierceadventurer.socialaccountservice.exception.ResourceNotFoundException;
import com.fierceadventurer.socialaccountservice.mapper.SocialAccountMapper;
import com.fierceadventurer.socialaccountservice.repository.SocialAccountRepository;
import com.fierceadventurer.socialaccountservice.service.SocialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialAccountServiceImpl implements SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;
    private final SocialAccountMapper socialAccountMapper;
    private final RateLimitProperties rateLimitProperties;

    @Override
    @Transactional
    public SocialAccountResponseDto createSocialAccount(CreateSocialAccountRequestDto requestDto) {
        SocialAccount socialAccount = socialAccountMapper.toEntity(requestDto);
        for(AuthToken token : socialAccount.getAuthTokens()){
            token.setSocialAccount(socialAccount);
        }

        RateLimitQuota quota = new RateLimitQuota();
        quota.setSocialAccount(socialAccount);

        String providerKey = socialAccount.getProvider().name().toLowerCase();
        RateLimitProperties.ProviderConfig config = rateLimitProperties
                .getProviders().getOrDefault(providerKey , rateLimitProperties.getProviders().get("default"));
        quota.setRequestLimit(config.getLimit());
        quota.setWindowStart(LocalDateTime.now().plusMinutes(config.getWindowMinutes()));

        socialAccount.setRateLimitQuota(quota);

        SocialAccount savedAccount = socialAccountRepository.save(socialAccount);
        return socialAccountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public void suspendAccount(UUID accountId, String reason) {
        SocialAccount account = socialAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Social account not found with id: " + accountId));
        account.setStatus(AccountStatus.SUSPENDED);
        socialAccountRepository.save(account);
    }

    @Override
    public void unsuspendAccount(UUID accountId) {
        SocialAccount account = socialAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Social account not found with id: " + accountId));
        if(account.getStatus() == AccountStatus.SUSPENDED){
            account.setStatus(AccountStatus.ACTIVE);
            socialAccountRepository.save(account);
        }

    }

    @Override
    @Transactional
    public void deleteSocialAccount(UUID accountId) {
        if(!socialAccountRepository.existsById(accountId)){
            throw new ResourceNotFoundException("Social account not found with id: " + accountId);
        }
        socialAccountRepository.deleteById(accountId);

    }

    @Override
    @Transactional
    public void markedAccountTokenExpired(UUID accountId) {
        SocialAccount account = socialAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + accountId));

        account.setStatus(AccountStatus.TOKEN_EXPIRED);
        socialAccountRepository.save(account);
    }
}
