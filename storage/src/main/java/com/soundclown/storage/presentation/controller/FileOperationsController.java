package com.soundclown.storage.presentation.controller;

import com.soundclown.storage.application.usecase.DeleteFileUseCase;
import com.soundclown.storage.application.usecase.DownloadFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/storage/files")
@RequiredArgsConstructor
public class FileOperationsController {

    private final DownloadFileUseCase downloadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;

    @GetMapping("/download/{objectKey}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID objectKey) {
        Resource resource = downloadFileUseCase.downloadFile(objectKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{objectKey}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID objectKey) {
        deleteFileUseCase.deleteFile(objectKey);
        return ResponseEntity.noContent().build();
    }
} 