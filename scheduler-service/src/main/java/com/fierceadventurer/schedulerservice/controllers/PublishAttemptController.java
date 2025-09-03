package com.fierceadventurer.schedulerservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs/{jobId}/attempts")
public class PublishAttemptController {

    @GetMapping
    public ResponseEntity<?> getAllAttemptsForJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<?> getAttemptById(@PathVariable UUID jobId, @PathVariable UUID attemptId) {
        return ResponseEntity.ok().build();
    }
}
