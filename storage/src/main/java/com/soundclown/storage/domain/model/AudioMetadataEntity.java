package com.soundclown.storage.domain.model;

import com.soundclown.storage.infrastructure.service.BinaryStoragePort.StorageBucketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audio_file_metadata", schema = "storage")
public class AudioMetadataEntity {
    @Id
    private UUID id;
    private String path;
    private long size;
    private String httpContentType;
    
    @Column(name = "song_id")
    private Long songId;
    
    @Enumerated(EnumType.STRING)
    private StorageBucketType storageBucketType;
}
