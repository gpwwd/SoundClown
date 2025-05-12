package com.soundclown.storage.application.service;

import com.soundclown.storage.application.repository.FileMetadataRepository;
import com.soundclown.storage.application.usecase.DeleteFileUseCase;
import com.soundclown.storage.application.usecase.DownloadFileUseCase;
import com.soundclown.storage.domain.model.AudioMetadataEntity;
import com.soundclown.storage.infrastructure.service.BinaryStoragePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileOperationsService implements DeleteFileUseCase, DownloadFileUseCase {
    
    private final BinaryStoragePort storagePort;
    private final FileMetadataRepository metadataRepository;

    @Override
    @Transactional
    public void deleteFile(UUID fileUuid) {
        AudioMetadataEntity metadata = metadataRepository.findById(fileUuid)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileUuid));
        
        try {
            storagePort.delete(metadata.getPath(), metadata.getId(), metadata.getStorageBucketType());
            metadataRepository.delete(metadata);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public Resource downloadFile(UUID fileUuid) {
        AudioMetadataEntity metadata = metadataRepository.findById(fileUuid)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileUuid));

        try {
            InputStream inputStream = storagePort.getInputStream(
                    metadata.getPath(),
                    metadata.getId(),
                    0,
                    metadata.getSize(),
                    metadata.getStorageBucketType()
            );
            return new ByteArrayResource(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
} 