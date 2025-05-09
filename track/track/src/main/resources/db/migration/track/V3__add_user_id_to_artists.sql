ALTER TABLE track.artists
    ADD COLUMN user_id BIGINT NOT NULL UNIQUE;
 
CREATE INDEX idx_artists_user_id ON track.artists(user_id); 