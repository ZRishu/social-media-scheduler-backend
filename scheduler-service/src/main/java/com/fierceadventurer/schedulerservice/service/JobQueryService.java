package com.fierceadventurer.schedulerservice.service;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.dto.UpdateJobRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobQueryService {
    ScheduledJobDto getJobById(UUID jobId);
    Page<ScheduledJobDto> getAllJobs(Pageable pageable);


}
