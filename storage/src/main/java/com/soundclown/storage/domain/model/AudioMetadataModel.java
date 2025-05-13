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
@Table(name = "audio_file_metadata", schema = "storage")
public class AudioMetadataModel {
    @Id
    private UUID id;
    private String path;
    private long size;
    private String httpContentType;
    
    @Column(name = "song_id")
    private Long songId;
    
    @Enumerated(EnumType.STRING)
    private StorageBucketType storageBucketType;

    public static AudioMetadataModel createForAudioUpload(
            MultipartFile file,
            Long songId,
            String originalFilename
    ) {
        return AudioMetadataModel.builder()
                .id(UUID.randomUUID())
                .size(file.getSize())
                .path(generatePath(originalFilename))
                .httpContentType(file.getContentType())
                .storageBucketType(StorageBucketType.AUDIO)
                .songId(songId)
                .build();
    }

    private static String generatePath(String originalFilename) {
        return originalFilename;
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
