package com.fierceadventurer.schedulerservice.entities;

import com.fierceadventurer.schedulerservice.enums.AttemptStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "publish_attempts" )
@Getter
@Setter
@NoArgsConstructor

public class PublishAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID attemptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id" , nullable = false)
    private ScheduledJob scheduledJob;

    @Column(nullable = false , updatable = false)
    private UUID postVariantId;

    @Column(nullable = false , updatable = false)
    private UUID socialAccountId;

    @Column(columnDefinition = "TEXT")
    private String providerRequest;

    @Column(columnDefinition = "TEXT")
    private String providerResponse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;


}
