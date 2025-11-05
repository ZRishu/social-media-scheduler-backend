package com.fierceadventurer.postservice.entity;

import com.fierceadventurer.postservice.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    @CreationTimestamp
    @Column(name = "date_posted" , updatable = false)
    private LocalDateTime datePosted;

    @UpdateTimestamp
    @Column(name = "last_edited")
    private LocalDateTime lastEdited;

    @OneToMany(
        mappedBy = "post",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<MediaAsset> mediaAssets = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PostVariant> variants = new ArrayList<>();

    public void addMediaAsset(MediaAsset mediaAsset) {
        this.mediaAssets.add(mediaAsset);
        mediaAsset.setPost(this);
    }

    public void addVariant(PostVariant variant) {
        this.variants.add(variant);
        variant.setPost(this);
    }
}
