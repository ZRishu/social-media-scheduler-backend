package com.fierceadventurer.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post_metrics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID socialAccountId;

    @Column(nullable = false)
    private String externalPostId;

    private LocalDateTime postedAt;

    private int likes;

    private int comments;

    private int shares;

    private int impressions;

    private double engagementScore;

    @UpdateTimestamp
    private LocalDateTime lastDataFetch;
}
