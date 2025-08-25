package com.fierceadventurer.postservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Data
@Entity
@Table(name = "post_variants")
public class PostVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "variant_id", updatable = false, nullable = false)
    private UUID variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id" , nullable = false)
    private  Post postId;

    @Column(nullable = false)
    private String platform;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "post_variant_hashtags" , joinColumns = @JoinColumn(name = "variant_id"))
    private Set<String> hashtags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "variant_media_assets",
            joinColumns = @JoinColumn(name = "variant_id"),
            inverseJoinColumns = @JoinColumn(name = "asset_id")
    )
    private List<MediaAsset> mediaRefs= new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime scheduledAt;
}
