package com.soundclown.storage.application.service;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import com.soundclown.storage.application.repository.storage.ArtistImageFileMetadataRepository;
import com.soundclown.storage.application.usecase.UploadImageFileUseCase;
import com.soundclown.storage.domain.model.ImageMetadataModel;
import com.soundclown.storage.infrastructure.service.BinaryStoragePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageStorageService implements UploadImageFileUseCase {
    
    private final BinaryStoragePort storagePort;
    private final ArtistImageFileMetadataRepository metadataRepository;

    @Override
    @Transactional
    public FileUploadResponse uploadArtistImage(MultipartFile file, Long artistId) {
        try {
            ImageMetadataModel metadata = ImageMetadataModel.createForArtistImageUpload(
                file,file.getOriginalFilename());

            metadataRepository.save(metadata);
            
            var storageData = metadata.getStorageData();
            storagePort.save(file, storageData.path(), storageData.id(), storageData.bucketType());
            
            var fileMetadata = metadata.getFileMetadata();
            return new FileUploadResponse(fileMetadata.id(), fileMetadata.path(),
                fileMetadata.size(), fileMetadata.contentType(),
                artistId, storageData.bucketType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload artist image", e);
        }
    }

    @Override
    @Transactional
    public FileUploadResponse uploadAlbumCover(MultipartFile file, Long albumId) {
        try {
            ImageMetadataModel metadata = ImageMetadataModel.createForAlbumCoverUpload(
                file, file.getOriginalFilename());

            metadataRepository.save(metadata);
            
            var storageData = metadata.getStorageData();
            storagePort.save(file, storageData.path(), storageData.id(), storageData.bucketType());
            
            var fileMetadata = metadata.getFileMetadata();
            return new FileUploadResponse(fileMetadata.id(), fileMetadata.path(),
                fileMetadata.size(), fileMetadata.contentType(),
                albumId, storageData.bucketType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload album cover", e);
        }
    }
} 