package com.fierceadventurer.socialaccountservice.repository;

import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, UUID> {
    Page<SocialAccount> findByUserId(UUID userId, Pageable pageable);
}
