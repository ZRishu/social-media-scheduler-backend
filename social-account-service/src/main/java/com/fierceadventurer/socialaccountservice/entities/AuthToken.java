package com.fierceadventurer.socialaccountservice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_tokens")
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tokenId;

    @Column(columnDefinition = "TEXT" , nullable = false)
    private String accessToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime expiry;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "auth_token_scopes" , joinColumns = @JoinColumn(name = "token_id"))
    private List<String> scopes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id" , nullable = false)
    private SocialAccount socialAccount;
}
