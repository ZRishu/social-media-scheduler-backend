package com.fierceadventurer.schedulerservice.mappers;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SchedulerMapper {

    @Mapping(target = "jobId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "retryCount", ignore = true)
    @Mapping(target = "lastError", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "attempts", ignore = true)
    @Mapping(source = "platform", target = "provider")
    ScheduledJob toEntity(VariantReadyForSchedulingEvent event);

    ScheduledJobDto toDto(ScheduledJob scheduledJob);
    PublishAttempt toDto(PublishAttempt publishAttempt);
}
