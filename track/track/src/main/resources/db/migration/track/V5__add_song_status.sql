ALTER TABLE track.songs ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'DRAFT';

-- Update existing songs with audio_metadata_id to ACTIVE status
UPDATE track.songs SET status = 'ACTIVE' WHERE audio_metadata_id IS NOT NULL; 