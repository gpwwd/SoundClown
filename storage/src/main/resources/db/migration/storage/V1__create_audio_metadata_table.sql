CREATE SCHEMA IF NOT EXISTS storage;

CREATE TABLE storage.audio_file_metadata (
    id UUID PRIMARY KEY,
    path VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    http_content_type VARCHAR(100) NOT NULL,
    storage_bucket_type VARCHAR(50) NOT NULL
); 