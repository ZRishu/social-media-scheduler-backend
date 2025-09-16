package com.fierceadventurer.socialaccountservice.service.Impl;

import com.fierceadventurer.socialaccountservice.config.RateLimitProperties;
import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import com.fierceadventurer.socialaccountservice.exception.ResourceNotFoundException;
import com.fierceadventurer.socialaccountservice.repository.RateLimitQuotaRepository;
import com.fierceadventurer.socialaccountservice.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final RateLimitQuotaRepository rateLimitQuotaRepository;
    private final RateLimitProperties rateLimitProperties;


    @Override
    @Transactional
    public void checkAndDecrementQuota(UUID accountId) {
        RateLimitQuota quota = rateLimitQuotaRepository.findBySocialAccount_AccountId(accountId)
                .orElseThrow(()-> new ResourceNotFoundException
                        ("Rate limit quota not found for account: " + accountId));

        if(quota.getWindowEnd() == null || LocalDateTime.now().isAfter(quota.getWindowEnd())){
            String providerKey =  quota.getSocialAccount().getProvider().name().toLowerCase();
            RateLimitProperties.ProviderConfig config = rateLimitProperties.getProviders()
                    .getOrDefault(providerKey , rateLimitProperties.getProviders().get("default"));

            quota.setUsedRequests(0);
            quota.setWindowStart(LocalDateTime.now());
            quota.setWindowEnd(LocalDateTime.now().plusMinutes(config.getWindowMinutes()));
        }

        if(quota.getUsedRequests() >= quota.getRequestLimit()){
            throw new ResourceNotFoundException("API rate limit exceeded for account " + accountId
            + ". Try again after " + quota.getWindowEnd());
        }

        quota.setUsedRequests(quota.getUsedRequests() + 1);
        rateLimitQuotaRepository.save(quota);
    }
}
