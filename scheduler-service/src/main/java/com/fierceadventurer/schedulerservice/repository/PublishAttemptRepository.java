package com.fierceadventurer.schedulerservice.repository;

import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublishAttemptRepository extends JpaRepository<PublishAttempt, UUID> {
}
