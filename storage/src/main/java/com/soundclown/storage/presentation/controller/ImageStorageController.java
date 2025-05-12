package com.soundclown.storage.presentation.controller;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import com.soundclown.storage.application.usecase.UploadImageFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/storage/images")
@RequiredArgsConstructor
public class ImageStorageController {

    private final UploadImageFileUseCase uploadFileUseCase;

    @PostMapping("/artist")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<FileUploadResponse> uploadArtistImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("artistId") Long artistId) {
        return ResponseEntity.ok(
                uploadFileUseCase.uploadArtistImage(file, artistId));
    }

    @PostMapping("/album")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<FileUploadResponse> uploadAlbumCover(
            @RequestParam("file") MultipartFile file,
            @RequestParam("albumId") Long albumId) {
        return ResponseEntity.ok(
                uploadFileUseCase.uploadAlbumCover(file, albumId));
    }
} 