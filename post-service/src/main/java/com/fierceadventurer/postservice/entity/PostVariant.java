package com.fierceadventurer.postservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post_variants")
public class PostVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "variant_id", updatable = false, nullable = false)
    private UUID variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id" , nullable = false)
    private  Post post;

    @Column(nullable = false)
    private String platform;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(name = "post_variant_hashtags" , joinColumns = @JoinColumn(name = "variant_id"))
    private List<String> hashtags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "variant_media_assets",
            joinColumns = @JoinColumn(name = "variant_id"),
            inverseJoinColumns = @JoinColumn(name = "asset_id")
    )
    private List<MediaAsset> mediaAssets= new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

}
