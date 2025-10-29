package com.fierceadventurer.schedulerservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "job_media_urls")
@Getter
@Setter
@NoArgsConstructor
public class JobMediaUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String mediaUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, updatable = false)
    private ScheduledJob scheduledJob;

    public JobMediaUrl(String mediaUrl, ScheduledJob scheduledJob) {
        this.mediaUrl = mediaUrl;
        this.scheduledJob = scheduledJob;
    }
}
