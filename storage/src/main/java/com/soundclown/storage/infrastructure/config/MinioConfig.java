package com.soundclown.storage.infrastructure.config;

import io.minio.MinioClient;
import io.minio.MakeBucketArgs;
import io.minio.BucketExistsArgs;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    @NotBlank
    private String endpoint;

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;

    @NotNull
    private Long putObjectPartSize; 

    private Bucket bucket = new Bucket();

    @Data
    public static class Bucket {
        @Value("${minio.bucket.audio-files}")
        private String audioFiles;
        @Value("${minio.bucket.artist-images}")
        private String artistImages;
        @Value("${minio.bucket.album-covers}")
        private String albumCovers;
    }

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        createBucketIfNotExists(client, bucket.getAudioFiles());
        createBucketIfNotExists(client, bucket.getArtistImages());
        createBucketIfNotExists(client, bucket.getAlbumCovers());

        return client;
    }

    private void createBucketIfNotExists(MinioClient client, String bucketName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
} 