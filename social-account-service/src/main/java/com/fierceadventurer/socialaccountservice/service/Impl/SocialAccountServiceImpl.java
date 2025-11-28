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
import com.fierceadventurer.socialaccountservice.enums.AccountType;
import com.fierceadventurer.socialaccountservice.enums.Provider;
import com.fierceadventurer.socialaccountservice.exception.ResourceNotFoundException;
import com.fierceadventurer.socialaccountservice.exception.TokenRefreshException;
import com.fierceadventurer.socialaccountservice.mapper.SocialAccountMapper;
import com.fierceadventurer.socialaccountservice.repository.AuthTokenRepository;
import com.fierceadventurer.socialaccountservice.repository.RateLimitQuotaRepository;
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
import java.util.Locale;
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
    private final RateLimitQuotaRepository rateLimitQuotaRepository;


    @Override
    @Transactional
    public SocialAccountResponseDto createSocialAccount(UUID userId , CreateSocialAccountRequestDto requestDto) {
        log.info("Creating social account for user {}", userId);

        if(Provider.valueOf(requestDto.getProvider().toUpperCase()) != Provider.LINKEDIN){
            throw new UnsupportedOperationException("Provider not yet supported: " + requestDto.getProvider());
        }

        LinkedInTokenResponse tokens = linkedInConnectClient.exchangeAuthCode(
                requestDto.getAuthCode(),
                requestDto.getRedirectUri()
        );
        LinkedInUserInfo userInfo = linkedInConnectClient.fetchUserProfile(tokens.getAccessToken());


        String personUrn = "urn:li:person:" + userInfo.getExternalId();
        SocialAccount personAccount = saveOrUpdateAccount(
                userId,
                personUrn,
                userInfo.getFullName(),
                "PERSONAL",
                userInfo.getPictureUrl(),
                tokens
        );

        try {
            LinkedInOrgResponse orgs = linkedInConnectClient.fetchUserCompanies(tokens.getAccessToken());

            if(orgs != null && orgs.getElements() != null){
                for(LinkedInOrgResponse.Element element : orgs.getElements()){
                    if(element.getOrganizationDetails() != null){
                        String orgName = element.getOrganizationDetails().getLocalizedName();
                        String orgId = element.getOrganizationDetails().getId();
                        String fullUrn = "urn:li:organization:" + orgId;

                        log.info("Found Organization: {} ({})", orgName , fullUrn);

                        saveOrUpdateAccount(
                                userId,
                                fullUrn,
                                orgName,
                                "BUSINESS",
                                null,
                                tokens
                        );
                    }
                }
            }
        }
        catch (Exception e){
            log.error("Error fetching/saving organizations. Proceeding with personal account only." , e);
        }

        return socialAccountMapper.toDto(personAccount);

    }

    private SocialAccount saveOrUpdateAccount(
            UUID userId , String externalId , String displayName ,
            String accountType , String profileImage , LinkedInTokenResponse tokens){

        SocialAccount account = socialAccountRepository.findByExternalId(externalId).orElse(new SocialAccount());

        boolean isNew = account.getAccountId() == null;

        account.setUserId(userId);
        account.setProvider(Provider.LINKEDIN);
        account.setAccountType(AccountType.valueOf(accountType));
        account.setExternalId(externalId);
        account.setDisplayName(displayName);
        account.setProfileImageUrl(profileImage);
        account.setStatus(AccountStatus.CONNECTED);

        SocialAccount savedAccount = socialAccountRepository.save(account);
        if(!isNew){
            authTokenRepository.deleteAll(savedAccount.getAuthTokens());
            savedAccount.getAuthTokens().clear();
        }
        AuthToken authToken = new AuthToken();
        authToken.setSocialAccount(savedAccount);
        authToken.setAccessToken(tokens.getAccessToken());
        authToken.setRefreshToken(tokens.getRefreshToken());
        authToken.setExpiry(LocalDateTime.now().plusSeconds(tokens.getExpiresIn()));
        authTokenRepository.save(authToken);

        if(isNew){
            RateLimitQuota quota = new RateLimitQuota();
            quota.setSocialAccount(savedAccount);

            RateLimitProperties.ProviderConfig config = rateLimitProperties
                    .getProviders().getOrDefault("linkedin" , rateLimitProperties.getProviders().get("default"));

            quota.setRequestLimit(config.getLimit());
            quota.setUsedRequests(0);
            quota.setWindowStart(LocalDateTime.now());
            rateLimitQuotaRepository.save(quota);

            AccountCreatedEvent event = new AccountCreatedEvent(
                    savedAccount.getAccountId(),
                    savedAccount.getProvider().name()
            );
            kafkaTemplate.send("social-account-created-topic", event);


        }
        return savedAccount;


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
