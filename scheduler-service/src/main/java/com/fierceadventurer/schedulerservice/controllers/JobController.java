package com.fierceadventurer.schedulerservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable UUID jobId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updatePendingJob(@PathVariable UUID jobId , @RequestBody Object updateJobDto) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{jobId}/retry")
    public ResponseEntity<?> retryFailedJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> cancelPendingJob(@PathVariable UUID jobId) {
        return ResponseEntity.noContent().build();
    }
}
