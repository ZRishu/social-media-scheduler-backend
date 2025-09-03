package com.fierceadventurer.schedulerservice.entities;

import com.fierceadventurer.schedulerservice.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scheduled_job")
@Getter
@Setter
@RequiredArgsConstructor

public class ScheduledJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobId;

    @Column(nullable = false , unique = true)
    private UUID postVariantId;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus jobStatus = JobStatus.PENDING;

    private int retryCount = 0;

    @Column(columnDefinition = "TEXT")
    private String lastError;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "scheduledJob" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<PublishAttempt> attempts = new ArrayList<>();
}
