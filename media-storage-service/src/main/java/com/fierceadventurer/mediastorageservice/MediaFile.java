package com.fierceadventurer.mediastorageservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Entity
@Table(name = "media_files")
@Getter
@Setter
@NoArgsConstructor
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    @Lob
    @Column(nullable = false , columnDefinition = "BYTEA")
    private byte[] data;
}
