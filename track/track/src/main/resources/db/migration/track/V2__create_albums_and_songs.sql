-- Установка search_path для выполнения операций в схеме track
SET search_path TO track;

-- Таблица альбомов
CREATE TABLE albums (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_date DATE,
    description TEXT,
    artist_id BIGINT NOT NULL REFERENCES artists(id) ON DELETE CASCADE
);

-- Таблица песен (отсутствует в исходном SQL, но есть ссылки на неё)
CREATE TABLE songs (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration INTEGER NOT NULL, -- продолжительность в секундах
    release_date DATE,
    lyrics TEXT,
    album_id BIGINT REFERENCES albums(id) ON DELETE CASCADE,
    artist_id BIGINT NOT NULL REFERENCES artists(id) ON DELETE CASCADE
); 