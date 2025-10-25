package com.fierceadventurer.schedulerservice.controllers;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.dto.UpdateJobRequestDto;
import com.fierceadventurer.schedulerservice.service.JobExecutionService;
import com.fierceadventurer.schedulerservice.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobQueryService jobQueryService;
    private final JobExecutionService jobExecutionService;


    @GetMapping
    public ResponseEntity<Page<ScheduledJobDto>> getAllJobs(Pageable pageable) {
        Page<ScheduledJobDto> jobs = jobQueryService.getAllJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ScheduledJobDto> getJobById(@PathVariable UUID jobId) {
        ScheduledJobDto job = jobQueryService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<ScheduledJobDto> updatePendingJob(@PathVariable UUID jobId , @RequestBody UpdateJobRequestDto requestDto) {
        ScheduledJobDto updatedJob = jobExecutionService.updateJobSchedule(jobId, requestDto);
        return ResponseEntity.ok(updatedJob);
    }

    @PostMapping("/{jobId}/retry")
    public ResponseEntity<Void> retryFailedJob(@PathVariable UUID jobId) {
        jobExecutionService.repostJob(jobId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> cancelPendingJob(@PathVariable UUID jobId) {
        jobExecutionService.cancelJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
