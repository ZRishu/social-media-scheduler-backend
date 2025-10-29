package com.fierceadventurer.socialaccountservice.repository;

import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, UUID> {

    @Query("SELECT sa FROM SocialAccount sa WHERE sa.userId = :userId AND sa.status != 'SUSPENDED'")
    Page<SocialAccount> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT sa FROM SocialAccount sa WHERE sa.accountId = :accountId AND sa.status != 'SUSPENDED'")
    Optional<SocialAccount> findActiveAccountById(UUID accountId);
}
