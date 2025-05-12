package com.soundclown.storage.application.service;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import com.soundclown.storage.application.repository.FileMetadataRepository;
import com.soundclown.storage.application.usecase.UploadImageFileUseCase;
import com.soundclown.storage.domain.model.AudioMetadataEntity;
import com.soundclown.storage.infrastructure.service.BinaryStoragePort;
import com.soundclown.storage.infrastructure.service.BinaryStoragePort.StorageBucketType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService implements UploadImageFileUseCase {
    
    private final BinaryStoragePort storagePort;
    private final FileMetadataRepository metadataRepository;

    @Override
    @Transactional
    public FileUploadResponse uploadArtistImage(MultipartFile file, Long artistId) {
        try {
            UUID fileUuid = UUID.randomUUID();
            
            AudioMetadataEntity metadata = AudioMetadataEntity.builder()
                    .id(fileUuid)
                    .size(file.getSize())
                    .path(generatePath("artists", file.getOriginalFilename()))
                    .httpContentType(file.getContentType())
                    .storageBucketType(StorageBucketType.ARTIST_IMAGE)
                    .build();

            metadataRepository.save(metadata);
            storagePort.save(file, metadata.getPath(), fileUuid, StorageBucketType.ARTIST_IMAGE);
            
            return new FileUploadResponse(artistId.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload artist image", e);
        }
    }

    @Override
    @Transactional
    public FileUploadResponse uploadAlbumCover(MultipartFile file, Long albumId) {
        try {
            UUID fileUuid = UUID.randomUUID();
            
            AudioMetadataEntity metadata = AudioMetadataEntity.builder()
                    .id(fileUuid)
                    .size(file.getSize())
                    .path(generatePath("albums", file.getOriginalFilename()))
                    .httpContentType(file.getContentType())
                    .storageBucketType(StorageBucketType.ALBUM_COVER)
                    .build();

            metadataRepository.save(metadata);
            storagePort.save(file, metadata.getPath(), fileUuid, StorageBucketType.ALBUM_COVER);
            
            return new FileUploadResponse(albumId.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload album cover", e);
        }
    }

    private String generatePath(String prefix, String originalFilename) {
        return String.format("%s/%s", prefix, originalFilename);
    }
} 