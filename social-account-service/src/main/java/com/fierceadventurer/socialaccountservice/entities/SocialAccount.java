package com.fierceadventurer.socialaccountservice.entities;

import com.fierceadventurer.socialaccountservice.enums.AccountStatus;
import com.fierceadventurer.socialaccountservice.enums.AccountType;
import com.fierceadventurer.socialaccountservice.enums.Provider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@NoArgsConstructor
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    @Column(nullable = false , updatable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String username;

    private String displayName;
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private String externalId;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "socialAccount" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<AuthToken> authTokens = new ArrayList<>();

    @OneToOne(mappedBy = "socialAccount" , cascade = CascadeType.ALL , orphanRemoval = true)
    private RateLimitQuota rateLimitQuota;
}
