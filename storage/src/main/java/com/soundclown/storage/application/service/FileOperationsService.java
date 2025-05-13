package com.soundclown.storage.application.service;

import com.soundclown.storage.application.repository.storage.AudioFileMetadataRepository;
import com.soundclown.storage.application.usecase.DeleteFileUseCase;
import com.soundclown.storage.application.usecase.DownloadFileUseCase;
import com.soundclown.storage.domain.model.AudioMetadataModel;
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
    private final AudioFileMetadataRepository metadataRepository;

    @Override
    @Transactional
    public void deleteFile(UUID fileUuid) {
        AudioMetadataModel metadata = metadataRepository.findById(fileUuid)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileUuid));
        
        try {
            var storageData = metadata.getStorageData();
            storagePort.delete(storageData.path(), storageData.id(), storageData.bucketType());
            metadataRepository.delete(metadata);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public Resource downloadFile(UUID fileUuid) {
        AudioMetadataModel metadata = metadataRepository.findById(fileUuid)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileUuid));

        try {
            var storageData = metadata.getStorageData();
            var fileMetadata = metadata.getFileMetadata();
            
            InputStream inputStream = storagePort.getInputStream(
                    storageData.path(),
                    storageData.id(),
                    0,
                    fileMetadata.size(),
                    storageData.bucketType()
            );
            return new ByteArrayResource(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
} 