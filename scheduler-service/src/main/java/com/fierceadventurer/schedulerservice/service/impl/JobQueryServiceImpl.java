package com.fierceadventurer.schedulerservice.service.impl;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.dto.UpdateJobRequestDto;
import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.enums.JobStatus;
import com.fierceadventurer.schedulerservice.mappers.SchedulerMapper;
import com.fierceadventurer.schedulerservice.repository.ScheduledJobRepository;
import com.fierceadventurer.schedulerservice.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobQueryServiceImpl implements JobQueryService {

    private final ScheduledJobRepository jobRepository;
    private final SchedulerMapper schedulerMapper;

    @Override
    @Transactional(readOnly = true)
    public ScheduledJobDto getJobById(UUID jobId) {
        return jobRepository.findById(jobId)
                .map(schedulerMapper::toDto)
                .orElseThrow(()-> new ResourceNotFoundException("job not found with id:" + jobId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ScheduledJobDto> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable).map(schedulerMapper::toDto);
    }



}
