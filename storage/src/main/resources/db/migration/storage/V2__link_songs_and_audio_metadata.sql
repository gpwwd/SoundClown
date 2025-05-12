-- V2__link_songs_and_audio_metadata.sql

DO $$
    BEGIN
        ----------------------------------------------------------------
        -- Шаг 1: Создать колонку audio_metadata_id в track.songs
        ----------------------------------------------------------------
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'track'
              AND table_name = 'songs'
              AND column_name = 'audio_metadata_id'
        ) THEN
            ALTER TABLE track.songs
                ADD COLUMN audio_metadata_id UUID;
        END IF;

        ----------------------------------------------------------------
        -- Шаг 2: Создать колонку song_id в storage.audio_file_metadata
        ----------------------------------------------------------------
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = 'storage'
              AND table_name = 'audio_file_metadata'
              AND column_name = 'song_id'
        ) THEN
            ALTER TABLE storage.audio_file_metadata
                ADD COLUMN song_id BIGINT;
        END IF;
    END
$$;

----------------------------------------------------------------
-- Теперь, когда оба столбца гарантированно существуют, добавляем
-- внешние ключи в любом порядке, т.к. ссылки теперь безопасны
----------------------------------------------------------------

DO $$
    BEGIN
        ----------------------------------------------------------------
        -- Шаг 3: FK из track.songs.audio_metadata_id → storage.audio_file_metadata(id)
        ----------------------------------------------------------------
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.table_constraints tc
            WHERE tc.constraint_type = 'FOREIGN KEY'
              AND tc.constraint_schema = 'track'
              AND tc.table_name = 'songs'
              AND tc.constraint_name = 'fk_song_audio_metadata'
        ) THEN
            ALTER TABLE track.songs
                ADD CONSTRAINT fk_song_audio_metadata
                    FOREIGN KEY (audio_metadata_id)
                        REFERENCES storage.audio_file_metadata(id)
                        ON DELETE SET NULL;
        END IF;

        ----------------------------------------------------------------
        -- Шаг 4: FK из storage.audio_file_metadata.song_id → track.songs(id)
        ----------------------------------------------------------------
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.table_constraints tc
            WHERE tc.constraint_type = 'FOREIGN KEY'
              AND tc.constraint_schema = 'storage'
              AND tc.table_name = 'audio_file_metadata'
              AND tc.constraint_name = 'fk_audio_metadata_song'
        ) THEN
            ALTER TABLE storage.audio_file_metadata
                ADD CONSTRAINT fk_audio_metadata_song
                    FOREIGN KEY (song_id)
                        REFERENCES track.songs(id)
                        ON DELETE SET NULL;
        END IF;
    END
$$;
