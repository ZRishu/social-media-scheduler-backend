package com.fierceadventurer.socialaccountservice.repository;

import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RateLimitQuotaRepository extends JpaRepository<RateLimitQuota, UUID> {
    Optional<RateLimitQuota> findBySocialAccount_AccountId(UUID accountId);
}
