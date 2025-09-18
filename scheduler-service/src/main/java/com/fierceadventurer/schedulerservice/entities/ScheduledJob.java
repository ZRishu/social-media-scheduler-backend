package com.fierceadventurer.schedulerservice.entities;

import com.fierceadventurer.schedulerservice.enums.JobStatus;
import com.fierceadventurer.schedulerservice.enums.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Entity
@Table(name = "scheduled_jobs")
@Getter
@Setter
@NoArgsConstructor
public class ScheduledJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobId;

    @Column(nullable = false , updatable = false)
    private UUID postVariantId;

    @Column(nullable = false, updatable = false)
    private UUID socialAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , updatable = false)
    private Provider provider;

    @Column(updatable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false, updatable = false)
    private String content;

    @OneToMany(mappedBy = "scheduledJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<JobHashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "scheduledJob", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<JobMediaUrl> mediaUrls = new ArrayList<>();


    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.PENDING;

    private LocalDateTime deletedAt;


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

    public void setHashtags(List<String> hashtagStrings) {
        if (hashtagStrings == null) return;
        this.hashtags = hashtagStrings.stream()
                .map(tag -> new JobHashtag(tag, this))
                .collect(Collectors.toList());
    }

    public void setMediaUrls(List<String> urlStrings) {
        if (urlStrings == null) return;
        this.mediaUrls = urlStrings.stream()
                .map(url -> new JobMediaUrl(url, this))
                .collect(Collectors.toList());
    }
}
