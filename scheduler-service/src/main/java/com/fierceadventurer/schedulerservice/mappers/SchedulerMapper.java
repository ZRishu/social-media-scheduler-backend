package com.fierceadventurer.schedulerservice.mappers;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SchedulerMapper {

    @Mapping(target = "jobId", ignore = true)
    @Mapping(source = "variantId", target = "postVariantId")
    @Mapping(source = "socialAccountId", target = "socialAccountId")
    @Mapping(target = "hashtags", ignore = true)
    @Mapping(target = "mediaUrls", ignore = true)
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

    @AfterMapping
    default void mapLists(VariantReadyForSchedulingEvent event, @MappingTarget ScheduledJob job) {
        // This uses the custom logic you wrote in your Entity to create JobHashtag/JobMediaUrl objects
        job.setHashtags(event.getHashtags());
        job.setMediaUrls(event.getMediaUrls());
    }
}
