package com.soundclown.storage.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.soundclown.storage.infrastructure.service.BinaryStoragePort.StorageBucketType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private UUID id;
    private String path;
    private long size;
    private String httpContentType;
    private Long songId;
    private StorageBucketType storageBucketType;
} 