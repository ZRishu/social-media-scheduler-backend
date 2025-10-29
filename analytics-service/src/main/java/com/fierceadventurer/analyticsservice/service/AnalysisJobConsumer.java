package com.fierceadventurer.analyticsservice.service;

import com.fierceadventurer.analyticsservice.dto.AccountCreatedEvent;
import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.mapper.AnalyticsMapper;
import com.fierceadventurer.analyticsservice.repository.AnalysisJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalysisJobConsumer {
    private final AnalysisJobRepository analysisJobRepository;
    private final AnalyticsMapper analyticsMapper;

    @KafkaListener(topics = "socialn-account-created-topic", groupId = "analytics-group")
    public void consumeAccountCreatedEvent(AccountCreatedEvent event){
        log.info("Received new account event for socialAccountId {}", event.getSocialAccountId());
        if(analysisJobRepository.findBySocialAccountId(event.getSocialAccountId()).isPresent()){
            log.warn("Analysis job already exists for account {}. Skipping.", event.getSocialAccountId());
            return;
        }

        AnalysisJob newJob = analyticsMapper.toEntity(event);

        analysisJobRepository.save(newJob);
        log.info("Successfully created new PENDING analysis job for account {}", newJob);
    }
}
