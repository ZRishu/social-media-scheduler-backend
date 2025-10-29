package com.fierceadventurer.schedulerservice.service;

import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;
import com.fierceadventurer.schedulerservice.mappers.SchedulerMapper;
import com.fierceadventurer.schedulerservice.repository.ScheduledJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ScheduledJobRepository scheduledJobRepository;
    private final JobExecutionService jobExecutionService;
    private final SchedulerMapper schedulerMapper;

    @KafkaListener(
            topics = "variant-scheduling-topic" , groupId = "scheduler-group" ,containerFactory = "kafkaListenerContainerFactory")
    public void consumeSchedulingEvent(VariantReadyForSchedulingEvent event){
        log.info("Consumed scheduling event for variantID : {} . Creating Job." , event.getVariantId());
        ScheduledJob job = schedulerMapper.toEntity(event);
        scheduledJobRepository.save(job);
        log.info("Successfully created ScheduledJob for variantId {}", job.getPostVariantId());

    }

    @KafkaListener(topics = "variant-immediate-publish-topic" , groupId = "scheduler-group" , containerFactory = "kafkaListenerContainerFactory")
    public void consumeImmediatePublishEvent(VariantReadyForSchedulingEvent event){
        log.info("Consumed immediate publish event for variantID : {} . Publishing now." , event.getVariantId());
        jobExecutionService.publishNow(event);
    }
}
