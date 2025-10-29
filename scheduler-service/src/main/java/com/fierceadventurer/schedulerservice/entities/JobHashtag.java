package com.fierceadventurer.schedulerservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "job_hashtags")
@Getter
@Setter
@NoArgsConstructor
public class JobHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false, updatable = false)
    private ScheduledJob scheduledJob;

    public JobHashtag(String hashtag, ScheduledJob scheduledJob) {
        this.hashtag = hashtag;
        this.scheduledJob = scheduledJob;
    }
}
