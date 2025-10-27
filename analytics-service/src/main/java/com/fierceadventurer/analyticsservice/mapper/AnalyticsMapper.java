package com.fierceadventurer.analyticsservice.mapper;

import com.fierceadventurer.analyticsservice.dto.AccountCreatedEvent;
import com.fierceadventurer.analyticsservice.dto.AnalysisJobDto;
import com.fierceadventurer.analyticsservice.dto.OptimalTimeSlotDto;
import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.entity.OptimalTimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

    @Mapping(target = "jobId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastError", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AnalysisJob toEntity(AccountCreatedEvent event);

    AnalysisJobDto toDto(AnalysisJob job);

    OptimalTimeSlotDto toDto(OptimalTimeSlot slot);

    List<OptimalTimeSlotDto> toDtoList(List<OptimalTimeSlot> slots);
}
