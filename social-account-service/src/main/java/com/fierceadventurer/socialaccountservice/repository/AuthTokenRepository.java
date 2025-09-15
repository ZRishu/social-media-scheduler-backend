package com.fierceadventurer.socialaccountservice.repository;

import com.fierceadventurer.socialaccountservice.entities.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken , UUID> {
}
