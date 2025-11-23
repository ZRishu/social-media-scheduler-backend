package com.fierceadventurer.schedulerservice.service.impl;

import com.fierceadventurer.schedulerservice.client.SocialAccountClient;
import com.fierceadventurer.schedulerservice.dto.PublishResponseDto;
import com.fierceadventurer.schedulerservice.dto.PublishRequestDto;
import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.dto.UpdateJobRequestDto;
import com.fierceadventurer.schedulerservice.entities.JobMediaUrl;
import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.enums.AttemptStatus;
import com.fierceadventurer.schedulerservice.enums.JobStatus;
import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;
import com.fierceadventurer.schedulerservice.exceptions.InvalidJobStatusException;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final ScheduledJobRepository jobRepository;
    private final PublishAttemptRepository attemptRepository;
    private final SchedulerMapper schedulerMapper;
    private final SocialAccountClient socialAccountClient;

    // --- 1. SCHEDULED EXECUTION (Runs every minute) ---
    @Override
    @Scheduled(fixedRate = 60000)
    public void findAndExecuteDueJobs() {
        log.info("Scheduler running: Checking for due jobs...");
        List<ScheduledJob> dueJobs = jobRepository.
                findTop10ByStatusAndScheduledAtBeforeAndDeletedAtIsNullOrderByScheduledAtAsc
                        (JobStatus.PENDING , LocalDateTime.now());

        if(dueJobs.isEmpty()) {
            return;
        }

        log.info("Found {} due jobs to process.", dueJobs.size());
        for(ScheduledJob job : dueJobs) {
            processJob(job);
        }
    }

    @Transactional
    public void processJob(ScheduledJob job) {
        job.setStatus(JobStatus.PROCESSING);
        jobRepository.save(job);
        // Delegates to the shared execution logic
        executePublishing(job);
    }

    // --- 2. IMMEDIATE EXECUTION (Triggered by Kafka) ---
    @Override
    @Transactional
    public void publishNow(VariantReadyForSchedulingEvent event) {
        log.info("Processing Immediate Publish for Variant: {}", event.getVariantId());

        // A. Convert Event to Job Entity immediately
        ScheduledJob job = schedulerMapper.toEntity(event);

        // B. Set status and time
        job.setStatus(JobStatus.PROCESSING);
        job.setScheduledAt(LocalDateTime.now());

        // C. Save to DB so we have a record of this job
        ScheduledJob savedJob = jobRepository.save(job);

        // D. Delegates to the shared execution logic
        executePublishing(savedJob);
    }


    public void executePublishing(ScheduledJob job){
        log.info("Executing Publishing Logic for Job: {}", job.getJobId());

        PublishAttempt attempt = new PublishAttempt();
        attempt.setScheduledJob(job);
        attempt.setPostVariantId(job.getPostVariantId());
        attempt.setSocialAccountId(job.getSocialAccountId());
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        attempt.setStartedAt(LocalDateTime.now());

        // Save initial attempt state
        attempt = attemptRepository.save(attempt);

        try {
            // 1. Check Rate Limits
            socialAccountClient.checkAndDecrementQuota(job.getSocialAccountId());

            log.info("Quota OK. Publishing Variant {}...", job.getPostVariantId());

            // 2. Prepare Payload for Social Account Service
            PublishRequestDto request = new PublishRequestDto();
            request.setContent(job.getContent());

            // Convert Entity List to String List
            List<String> mediaUrlStrings = job.getMediaUrls().stream()
                    .map(JobMediaUrl::getMediaUrl)
                    .collect(Collectors.toList());
            request.setMediaUrls(mediaUrlStrings);

            // 3. REAL API CALL (Synchronous)
            PublishResponseDto response = socialAccountClient.publishPost(job.getSocialAccountId(), request);
            String providerId = response.getId();
            log.info("Published successfully! Provider ID: {}", providerId);

            // 4. Success State
            attempt.setProviderResponse(providerId);
            attempt.setStatus(AttemptStatus.SUCCESS);
            job.setStatus(JobStatus.COMPLETED);
        }
        catch (Exception e) {
            log.error("Job {} failed: {}", job.getJobId(), e.getMessage(), e);

            // 5. Failure State
            attempt.setStatus(AttemptStatus.FAILURE);
            attempt.setProviderResponse("Error: " + e.getMessage()); // Store error for debugging

            job.setStatus(JobStatus.FAILED);
            job.setLastError(e.getMessage());

            // Optional: Add retry logic here if needed (e.g., increment retry count)
        }
        finally {
            // 6. Final Commit
            attempt.setCompletedAt(LocalDateTime.now());
            attemptRepository.save(attempt);
            jobRepository.save(job);
        }
    }

    // --- 4. JOB MANAGEMENT ---

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

        // Allow reposting FAILED jobs too, not just cancelled ones
        if(job.getDeletedAt() == null && job.getStatus() != JobStatus.FAILED){
            // Logic might vary based on requirements, but generally we repost failed/cancelled jobs
            // For now keeping your logic:
            throw new InvalidJobStatusException("Job must be cancelled or failed to repost");
        }

        job.setDeletedAt(null);
        job.setStatus(JobStatus.PENDING);
        job.setLastError(null);
        // Reset retry count if you implement retries
        job.setRetryCount(0);
        // Reset schedule to now (or handle new schedule)
        job.setScheduledAt(LocalDateTime.now());

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