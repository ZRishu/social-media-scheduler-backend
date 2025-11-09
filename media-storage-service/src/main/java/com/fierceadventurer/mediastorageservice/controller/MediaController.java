package com.fierceadventurer.mediastorageservice.controller;

import com.fierceadventurer.mediastorageservice.dto.MediaUploadResponseDto;
import com.fierceadventurer.mediastorageservice.entity.MediaFile;
import com.fierceadventurer.mediastorageservice.service.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaStorageService mediaStorageService;

    @PostMapping("/upload")
    public ResponseEntity<MediaUploadResponseDto> uploadMedia(@RequestParam("file") MultipartFile file) {
        MediaUploadResponseDto response = mediaStorageService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMediaFile(@PathVariable UUID id) {
        MediaFile mediaFile = mediaStorageService.getFile(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mediaFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaFile.getFileName() + "\"")
                .body(mediaFile.getData());

    }
}
