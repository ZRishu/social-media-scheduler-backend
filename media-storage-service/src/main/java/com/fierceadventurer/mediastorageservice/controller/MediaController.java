package com.fierceadventurer.mediastorageservice.controller;

import com.fierceadventurer.mediastorageservice.dto.MediaUploadResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    @PostMapping("/upload")
    public ResponseEntity<MediaUploadResponseDto> uploadMedia(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMediaFile(@PathVariable UUID id) {
        return ResponseEntity.ok().build();
    }
}
