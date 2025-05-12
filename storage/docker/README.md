# MinIO Storage Setup

## Overview
This directory contains Docker configuration for MinIO object storage used in SoundClown application.

## Configuration
Default credentials (DO NOT USE IN PRODUCTION):
- Access Key: minioadmin
- Secret Key: minioadmin

Ports:
- 9000: API Port
- 9001: Console Port

Pre-configured buckets:
- audio-files: for storing audio tracks
- artist-images: for storing artist profile images
- album-covers: for storing album cover images

## Usage

### Start MinIO
```bash
docker-compose up -d
```

### Stop MinIO
```bash
docker-compose down
```

### Access MinIO Console
Open http://localhost:9001 in your browser

### View Logs
```bash
docker-compose logs -f
```

## Development Notes
- MinIO console is available at http://localhost:9001
- API endpoint is available at http://localhost:9000
- Data is persisted in a named volume: soundclown_minio_data
- Health check is configured to ensure MinIO is ready before use
- Buckets are automatically created on startup

## Security Notes
- Default credentials should be changed in production
- SSL/TLS should be configured for production use
- Proper access policies should be configured for buckets 