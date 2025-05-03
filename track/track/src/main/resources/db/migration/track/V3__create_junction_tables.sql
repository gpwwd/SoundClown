-- Установка search_path для выполнения операций в схеме track
SET search_path TO track;

-- Таблица связи альбомов и жанров
CREATE TABLE album_genre (
    album_id BIGINT NOT NULL REFERENCES albums(id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (album_id, genre_id)
);

-- Таблица связи исполнителей и жанров
CREATE TABLE artist_genre (
    artist_id BIGINT NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (artist_id, genre_id)
);

-- Таблица связи песен и жанров
CREATE TABLE song_genre (
    song_id BIGINT NOT NULL REFERENCES songs(id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (song_id, genre_id)
); 