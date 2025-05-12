package com.soundclown.storage.infrastructure.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

public interface BinaryStoragePort {
    void save(MultipartFile file, String path, UUID id, StorageBucketType type) throws Exception;
    void delete(String path, UUID id, StorageBucketType type) throws Exception;
    InputStream getInputStream(String path, UUID id, long offset, long length, StorageBucketType type) throws Exception;
    String getBucketName(StorageBucketType type);

    enum StorageBucketType {
        AUDIO,
        ARTIST_IMAGE,
        ALBUM_COVER
    }
}