package com.fierceadventurer.socialaccountservice.service.Impl;

import com.fierceadventurer.socialaccountservice.client.TokenRefreshClient;
import com.fierceadventurer.socialaccountservice.client.TokenRefreshClientFactory;
import com.fierceadventurer.socialaccountservice.client.impl.LinkedInConnectClient;
import com.fierceadventurer.socialaccountservice.config.RateLimitProperties;
import com.fierceadventurer.socialaccountservice.dto.*;
import com.fierceadventurer.socialaccountservice.entities.AuthToken;
import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import com.fierceadventurer.socialaccountservice.enums.AccountStatus;
import com.fierceadventurer.socialaccountservice.enums.Provider;
import com.fierceadventurer.socialaccountservice.exception.ResourceNotFoundException;
import com.fierceadventurer.socialaccountservice.exception.TokenRefreshException;
import com.fierceadventurer.socialaccountservice.mapper.SocialAccountMapper;
import com.fierceadventurer.socialaccountservice.repository.AuthTokenRepository;
import com.fierceadventurer.socialaccountservice.repository.SocialAccountRepository;
import com.fierceadventurer.socialaccountservice.service.SocialAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialAccountServiceImpl implements SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;
    private final AuthTokenRepository authTokenRepository;
    private final SocialAccountMapper socialAccountMapper;
    private final RateLimitProperties rateLimitProperties;
    private final KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate;
    private final TokenRefreshClientFactory tokenRefreshFactory;
    private final LinkedInConnectClient linkedInConnectClient;

    @Override
    @Transactional
    public SocialAccountResponseDto createSocialAccount(UUID userId , CreateSocialAccountRequestDto requestDto) {
        log.info("Creating social account for user {}", userId);
        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setUserId(userId);
        socialAccount.setProvider(Provider.valueOf(requestDto.getProvider().toUpperCase()));
        socialAccount.setStatus(AccountStatus.ACTIVE);
        socialAccount.setAccountType(requestDto.getAccountType());

        if(socialAccount.getProvider() == Provider.LINKEDIN && requestDto.getAuthCode() != null) {
            log.info("Starting LinkedIn OAuth flow for user {}", userId);

            LinkedInTokenResponse tokens = linkedInConnectClient.exchangeAuthCode(
                    requestDto.getAuthCode(),
                    requestDto.getRedirectUri()
            );

            LinkedInUserInfo userInfo = linkedInConnectClient.fetchUserProfile(tokens.getAccessToken());

            socialAccount.setExternalId(userInfo.getExternalId());
            socialAccount.setUsername(userInfo.getEmail());
            socialAccount.setDisplayName(userInfo.getFullName());
            socialAccount.setProfileImageUrl(userInfo.getPictureUrl());

            AuthToken authToken = new AuthToken();
            authToken.setSocialAccount(socialAccount);
            authToken.setAccessToken(tokens.getAccessToken());
            authToken.setRefreshToken(tokens.getRefreshToken());
            authToken.setExpiry(LocalDateTime.now().plusSeconds(tokens.getExpiresIn()));

            socialAccount.getAuthTokens().add(authToken);

        }
        else{
            throw new UnsupportedOperationException("Provider not yet supported: " + socialAccount.getProvider());
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

        AccountCreatedEvent event = new AccountCreatedEvent(
                savedAccount.getAccountId(),
                savedAccount.getProvider().name()
        );

        kafkaTemplate.send("social-account-created-topic", event);
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

    @Override
    public String getActiveAccessToken(UUID accountId) {
        SocialAccount account = socialAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        AuthToken currentToken = account.getAuthTokens().stream()
                .max(Comparator.comparing(AuthToken::getExpiry))
                .orElseThrow(() -> new ResourceNotFoundException("No Tokens found for account: " + accountId));
        if(currentToken.getExpiry().isAfter(LocalDateTime.now())){
            log.info("Found valid access token for account with id: " + accountId);
            return currentToken.getAccessToken();
        }

        log.warn("Access token for account {} is expired. Attempting refresh...", accountId);

        try{
            return refreshAccessToken(currentToken);
        }catch (Exception ex){
            log.error("Failed to refresh token for account {}. Marking as expired.", accountId, ex);
            account.setStatus(AccountStatus.TOKEN_EXPIRED);
            socialAccountRepository.save(account);

            throw new TokenRefreshException("Could not refresh token for account " + accountId + ". User must re-authenticate.");
        }
    }

    @Override
    public void validateAccountOwnership(UUID accountId, UUID userId) {
        log.info("Validating ownership for account {} by user {}", accountId, userId);

        SocialAccount account = socialAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Social Account not found with id: " + accountId));

        if(!account.getUserId().equals(userId)){
            log.warn("Access Denied: User {} does not own social account {}", userId, accountId);
            throw new AccessDeniedException("User does not have permisiion for this social account.");
        }
    }

    @Transactional
    protected String refreshAccessToken(AuthToken expiredToken) {
        SocialAccount account = expiredToken.getSocialAccount();

        TokenRefreshClient client = tokenRefreshFactory.getClient(account.getProvider());

        OAuthRefreshResponse refreshResponse = client.refreshAccessToken(expiredToken.getAccessToken());

        expiredToken.setAccessToken(refreshResponse.getAccessToken());

        if(refreshResponse.getRefreshToken() != null) {
            expiredToken.setRefreshToken(refreshResponse.getRefreshToken());
        }
        expiredToken.setExpiry(LocalDateTime.now().plusSeconds(refreshResponse.getExpiresIn()));
        authTokenRepository.save(expiredToken);
        log.info("Successfully refreshed access token for account {}",account.getAccountId());

        return expiredToken.getAccessToken();


    }


    @Override
    public String publishContent(UUID accountId, PublishRequestDto requestDto) {
        // 1. Get valid token
        String accessToken = getActiveAccessToken(accountId);
        SocialAccount account = socialAccountRepository.findById(accountId).orElseThrow();

        // 2. Call LinkedIn Client
        return linkedInConnectClient.publishPost(account.getExternalId(), accessToken, requestDto);
    }
}
