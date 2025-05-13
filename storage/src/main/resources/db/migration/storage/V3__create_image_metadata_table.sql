-- V3__create_image_metadata_table.sql

CREATE TABLE storage.image_file_metadata (
    id UUID PRIMARY KEY,
    path VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    http_content_type VARCHAR(100) NOT NULL,
    storage_bucket_type VARCHAR(50) NOT NULL
);

CREATE INDEX idx_storage_bucket_type 
    ON storage.image_file_metadata (storage_bucket_type); 