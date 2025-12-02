package com.fierceadventurer.socialaccountservice.entities;

import com.fierceadventurer.socialaccountservice.util.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
@NoArgsConstructor
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tokenId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(columnDefinition = "TEXT" )
    private String accessToken;

    @Convert(converter = AttributeEncryptor.class)
    @Column(columnDefinition = "TEXT" )
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime expiry;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "auth_token_scopes" , joinColumns = @JoinColumn(name = "token_id"))
    private List<String> scopes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id" , nullable = false)
    private SocialAccount socialAccount;
}
