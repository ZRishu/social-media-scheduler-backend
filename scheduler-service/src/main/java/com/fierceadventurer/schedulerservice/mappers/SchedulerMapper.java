package com.fierceadventurer.schedulerservice.mappers;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchedulerMapper {
    ScheduledJobDto toDto(ScheduledJob scheduledJob);
    PublishAttempt toDto(PublishAttempt publishAttempt);
}
