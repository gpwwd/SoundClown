package com.soundclown.storage.application.service;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import com.soundclown.storage.application.repository.FileMetadataRepository;
import com.soundclown.storage.application.usecase.StreamFileUseCase;
import com.soundclown.storage.application.usecase.UploadAudioFileUseCase;
import com.soundclown.storage.domain.model.AudioMetadataEntity;
import com.soundclown.storage.domain.util.ChunkWithMetadata;
import com.soundclown.storage.domain.util.Range;
import com.soundclown.storage.infrastructure.service.BinaryStoragePort;
import com.soundclown.storage.infrastructure.service.BinaryStoragePort.StorageBucketType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioStorageService implements UploadAudioFileUseCase, StreamFileUseCase {
    
    private final BinaryStoragePort storagePort;
    private final FileMetadataRepository metadataRepository;

    @Override
    @Transactional
    public FileUploadResponse uploadAudioFile(MultipartFile file, Long trackId) {
        try {
            UUID fileUuid = UUID.randomUUID();
            
            AudioMetadataEntity metadata = AudioMetadataEntity.builder()
                    .id(fileUuid)
                    .size(file.getSize())
                    .path(generatePath(file.getOriginalFilename()))
                    .httpContentType(file.getContentType())
                    .storageBucketType(StorageBucketType.AUDIO)
                    .build();

            metadataRepository.save(metadata);
            storagePort.save(file, metadata.getPath(), fileUuid, StorageBucketType.AUDIO);
            
            return new FileUploadResponse(trackId.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload audio file", e);
        }
    }

    @Override
    public ChunkWithMetadata fetchChunk(UUID trackId, Range range) {
        AudioMetadataEntity metadata = metadataRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Audio file not found: " + trackId));

        return new ChunkWithMetadata(
                metadata,
                readChunk(metadata.getPath(), trackId,
                 range, metadata.getSize(), metadata.getStorageBucketType())
        );
    }

    private byte[] readChunk(String path, UUID uuid, Range range, long fileSize, StorageBucketType type) {
        long startPosition = range.getRangeStart();
        long endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);

        try (InputStream inputStream = storagePort.getInputStream(path, uuid, startPosition, chunkSize, type)) {
            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read audio chunk", e);
        }
    }

    private String generatePath(String originalFilename) {
        return String.format(originalFilename);
    }
} 