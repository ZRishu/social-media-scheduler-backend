package com.fierceadventurer.schedulerservice.controllers;

import com.fierceadventurer.schedulerservice.dto.PublishAttemptDto;
import com.fierceadventurer.schedulerservice.service.JobQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs/{jobId}/attempts")
@RequiredArgsConstructor
public class PublishAttemptController {
    private final JobQueryService  jobQueryService;

    @GetMapping
    public ResponseEntity<List<PublishAttemptDto>> getAttemptById(@PathVariable UUID jobId) {
        List<PublishAttemptDto> attempts = jobQueryService.getJobById(jobId).getAttempts();
        return ResponseEntity.ok(attempts);
    }
}
