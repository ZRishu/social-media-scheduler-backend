package com.fierceadventurer.socialaccountservice.repository;

import com.fierceadventurer.socialaccountservice.entities.RateLimitQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateLimitQuotaRepository extends JpaRepository<RateLimitQuota, UUID> {
    Optional<RateLimitQuota> findBySocialAccount_AccountId(UUID accountId);
}
