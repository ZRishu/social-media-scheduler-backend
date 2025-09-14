package com.fierceadventurer.socialaccountservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "rate_limit_quotas")
@NoArgsConstructor
public class RateLimitQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID quotaId;

    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;
    private int usedRequests = 0;
    private int requestLimit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id" , nullable = false)
    private SocialAccount socialAccount;
}