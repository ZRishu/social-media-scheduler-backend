package com.fierceadventurer.postservice.entity;
import com.fierceadventurer.postservice.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@Table(name = "media_assets")
@Entity
public class MediaAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID assetId;

    @Column(name = "media_url", nullable = false, columnDefinition = "TEXT")
    private String storageUrl;

    @Column(nullable = false)
    private Long size;

    @CreationTimestamp
    @Column(name = "upload_date", updatable = false)
    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type" , nullable = false)
    private PostType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToMany(mappedBy = "mediaAssets")
    private List<PostVariant> variants = new ArrayList<>();
}
