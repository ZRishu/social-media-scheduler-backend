package com.fierceadventurer.analyticsservice.entity;

import com.fierceadventurer.analyticsservice.enums.AnalysisStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public class AnalysisJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false , updatable = false , unique = true)
    private UUID socialAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false )
    private AnalysisStatus status = AnalysisStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String lastError;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
