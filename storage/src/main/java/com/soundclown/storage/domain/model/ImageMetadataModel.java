package com.soundclown.storage.domain.model;

import com.soundclown.storage.infrastructure.service.BinaryStoragePort.StorageBucketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import lombok.AccessLevel;

import java.util.UUID;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "image_file_metadata", schema = "storage")
public class ImageMetadataModel {
    @Id
    private UUID id;
    private String path;
    private long size;
    private String httpContentType;
    
    @Enumerated(EnumType.STRING)
    private StorageBucketType storageBucketType;

    public static ImageMetadataModel createForArtistImageUpload(
            MultipartFile file,
            String originalFilename
    ) {
        return ImageMetadataModel.builder()
                .id(UUID.randomUUID())
                .size(file.getSize())
                .path(generatePath("artists", originalFilename))
                .httpContentType(file.getContentType())
                .storageBucketType(StorageBucketType.ARTIST_IMAGE)
                .build();
    }

    public static ImageMetadataModel createForAlbumCoverUpload(
            MultipartFile file,
            String originalFilename
    ) {
        return ImageMetadataModel.builder()
                .id(UUID.randomUUID())
                .size(file.getSize())
                .path(generatePath("albums", originalFilename))
                .httpContentType(file.getContentType())
                .storageBucketType(StorageBucketType.ALBUM_COVER)
                .build();
    }

    private static String generatePath(String prefix, String originalFilename) {
        return String.format("%s/%s", prefix, originalFilename);
    }

    public record StorageData(String path, UUID id, StorageBucketType bucketType) {}

    public StorageData getStorageData() {
        return new StorageData(path, id, storageBucketType);
    }

    public record FileMetadata(UUID id, String path, long size, String contentType) {}

    public FileMetadata getFileMetadata() {
        return new FileMetadata(id, path, size, httpContentType);
    }
}
