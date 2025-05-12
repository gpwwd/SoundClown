package com.soundclown.storage.infrastructure.service;

import com.soundclown.storage.infrastructure.config.MinioConfig;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageAdapter implements BinaryStoragePort {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public String getBucketName(StorageBucketType type) {
        return switch (type) {
            case AUDIO -> minioConfig.getBucket().getAudioFiles();
            case ARTIST_IMAGE -> minioConfig.getBucket().getArtistImages();
            case ALBUM_COVER -> minioConfig.getBucket().getAlbumCovers();
        };
    }

    @Override
    public void save(MultipartFile file, String path, UUID id, StorageBucketType type) throws Exception {
        String bucketName = getBucketName(type);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path + id.toString())
                        .stream(file.getInputStream(), file.getSize(), minioConfig.getPutObjectPartSize())
                        .build()
        );
    }

    @Override
    public void delete(String path, UUID id, StorageBucketType type) throws Exception {
        String bucketName = getBucketName(type);
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path + id.toString())
                        .build()
        );
    }

    @Override
    public InputStream getInputStream(String path, UUID id, long offset, long length, StorageBucketType type) throws Exception {
        String bucketName = getBucketName(type);
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .offset(offset)
                        .length(length)
                        .object(path + id.toString())
                        .build());
    }
}