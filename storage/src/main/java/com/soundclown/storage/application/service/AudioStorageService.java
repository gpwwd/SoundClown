package com.soundclown.storage.application.service;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import com.soundclown.storage.application.usecase.StreamFileUseCase;
import com.soundclown.storage.application.usecase.UploadAudioFileUseCase;
import com.soundclown.storage.domain.model.AudioMetadataModel;
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
import java.util.Optional;
import com.soundclown.storage.domain.model.SongValidationModel;
import com.soundclown.storage.application.repository.storage.AudioFileMetadataRepository;
import com.soundclown.storage.application.repository.track.SongValidationRepository;

@Service
@RequiredArgsConstructor
public class AudioStorageService implements UploadAudioFileUseCase, StreamFileUseCase {
    
    private final BinaryStoragePort storagePort;
    private final AudioFileMetadataRepository metadataRepository;
    private final SongValidationRepository songValidationRepository;

    @Override
    @Transactional
    public FileUploadResponse uploadAudioFile(MultipartFile file, Long trackId) {
        try {
            SongValidationModel songValidationEntity = songValidationRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track with id " + trackId + " does not exist"));
            songValidationEntity.validateStatusForAudioUpload();

            AudioMetadataModel metadata = AudioMetadataModel.createForAudioUpload(
                file, 
                trackId, 
                file.getOriginalFilename()
            );

            metadataRepository.save(metadata);
            
            var storageData = metadata.getStorageData();
            storagePort.save(file, storageData.path(), storageData.id(), storageData.bucketType());
            
            var fileMetadata = metadata.getFileMetadata();
            return new FileUploadResponse(fileMetadata.id(), fileMetadata.path(),
                fileMetadata.size(), fileMetadata.contentType(),
                trackId, storageData.bucketType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload audio file", e);
        }
    }

    @Override
    public ChunkWithMetadata fetchChunk(UUID trackId, Range range) {
        AudioMetadataModel metadata = metadataRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Audio file not found: " + trackId));

        var storageData = metadata.getStorageData();
        var fileMetadata = metadata.getFileMetadata();
        
        return new ChunkWithMetadata(
                metadata,
                readChunk(storageData.path(), trackId,
                 range, fileMetadata.size(), storageData.bucketType())
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
} 