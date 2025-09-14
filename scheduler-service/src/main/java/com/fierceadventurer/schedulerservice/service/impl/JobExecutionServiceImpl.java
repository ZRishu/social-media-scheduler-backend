package com.fierceadventurer.schedulerservice.service.impl;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.dto.UpdateJobRequestDto;
import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.enums.AttemptStatus;
import com.fierceadventurer.schedulerservice.enums.JobStatus;
import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;
import com.fierceadventurer.schedulerservice.exceptions.ResourceNotFoundException;
import com.fierceadventurer.schedulerservice.mappers.SchedulerMapper;
import com.fierceadventurer.schedulerservice.repository.PublishAttemptRepository;
import com.fierceadventurer.schedulerservice.repository.ScheduledJobRepository;
import com.fierceadventurer.schedulerservice.service.JobExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final ScheduledJobRepository jobRepository;
    private final PublishAttemptRepository attemptRepository;
    private final SchedulerMapper  schedulerMapper;

    @Override
    @Scheduled(fixedRate = 60000)
    public void findAndExecutionDueJobs() {
        log.info("Scheduler running: Checking for due jobs...");
        List<ScheduledJob> dueJobs = jobRepository.findTop10ByStatusAndScheduledAtBeforeAndDeletedAtIsNullOrderByScheduledAtAsc
                (JobStatus.PENDING , LocalDateTime.now());
        if(dueJobs.isEmpty()) {
            return;
        }
        log.info("Found{} due jobs to process." , dueJobs.size());
        for(ScheduledJob job : dueJobs) {
            processJob(job);
        }
    }

    @Transactional

    public void processJob(ScheduledJob job) {
        job.setStatus(JobStatus.PROCESSING);
        jobRepository.save(job);

        VariantReadyForSchedulingEvent eventData = new VariantReadyForSchedulingEvent();
        eventData.setVariantId(job.getPostVariantId());
//        eventData.setSocialAccountId(job.getSocialAccountId());
        eventData.setSocialAccountId(UUID.randomUUID());
        executePublishing(job , eventData);
    }

    private void executePublishing(ScheduledJob job, VariantReadyForSchedulingEvent eventData) {
        PublishAttempt attempt = new PublishAttempt();
        if(job != null){
            attempt.setScheduledJob(job);
        }
        attempt.setPostVariantId(eventData.getVariantId());
        attempt.setSocialAccountId(eventData.getSocialAccountId());
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        attempt.setStartedAt(LocalDateTime.now());
        attemptRepository.save(attempt);

        try {
            log.info("Simulating publication for Variant {}", eventData.getVariantId());
            Thread.sleep(1000);
            attempt.setCompletedAt(LocalDateTime.now());
            attempt.setStatus(AttemptStatus.SUCCESS);
            attemptRepository.save(attempt);
            if(job != null){
                job.setStatus(JobStatus.COMPLETED);
                jobRepository.save(job);
            }
        }catch (Exception e){
            log.error("Job for variant {} execution failed.", eventData.getVariantId(), e.getMessage());
            attempt.setCompletedAt(LocalDateTime.now());
            attempt.setStatus(AttemptStatus.FAILURE);
            attemptRepository.save(attempt);
            if(job != null){
                job.setStatus(JobStatus.FAILED);
                job.setLastError(e.getMessage());
                jobRepository.save(job);
            }
        }
    }



    @Override
    public void publishNow(VariantReadyForSchedulingEvent event) {
        executePublishing(null , event);
    }

    @Override
    @Transactional
    public void cancelJob(UUID jobId) {
        log.info("Attempting to cancel job with ID {}", jobId);
        ScheduledJob job = jobRepository.findById(jobId).orElseThrow(
                ()-> new ResourceNotFoundException("Job not found with id " + jobId));

        if(job.getStatus() != JobStatus.PENDING){
            throw new ResourceNotFoundException("Only pending job can be cancelled");
        }

        job.setStatus(JobStatus.FAILED);
        job.setLastError("Cancelled by user at " + LocalDateTime.now());
        job.setDeletedAt(LocalDateTime.now());
        jobRepository.save(job);
        log.info("Successfully cancelled job with ID {}", jobId);

    }

    @Override
    @Transactional
    public ScheduledJobDto repostJob(UUID jobId) {
        log.info("Attempting to repost job with ID {}", jobId);
        ScheduledJob job = jobRepository.findById(jobId).orElseThrow(
                ()-> new ResourceNotFoundException("Job not found with id " + jobId)

        );

        job.setDeletedAt(null);
        job.setStatus(JobStatus.PENDING);
        job.setLastError(null);

        ScheduledJob repostedJob = jobRepository.save(job);
        log.info("Successfully reposted job with ID {}", jobId);
        return schedulerMapper.toDto(repostedJob);
    }

    @Override
    @Transactional
    public ScheduledJobDto updateJobSchedule(UUID jobId, UpdateJobRequestDto requestDto) {
        ScheduledJob job = jobRepository.findById(jobId).orElseThrow(
                ()-> new ResourceNotFoundException("job not found with id:" + jobId));
        if(job.getStatus() != JobStatus.PENDING){
            throw new IllegalStateException("Cannot update a job that is not in pending status.");
        }
        job.setScheduledAt(requestDto.getScheduledAt());
        ScheduledJob updatedJob = jobRepository.save(job);
        return schedulerMapper.toDto(updatedJob);
    }


}
