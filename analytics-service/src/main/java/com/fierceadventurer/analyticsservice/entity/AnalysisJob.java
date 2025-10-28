package com.fierceadventurer.analyticsservice.entity;

import com.fierceadventurer.analyticsservice.enums.AnalysisStatus;
import com.fierceadventurer.analyticsservice.enums.Provider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "analysis_jobs")
@NoArgsConstructor
public class AnalysisJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobId;

    @Column(nullable = false , updatable = false , unique = true)
    private UUID socialAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Provider provider;

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
