-- Создание схемы track, если её ещё нет
CREATE SCHEMA IF NOT EXISTS track;

-- Установка search_path для выполнения операций в схеме track
SET search_path TO track;

-- Таблица жанров
CREATE TABLE genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица исполнителей
CREATE TABLE artists (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
); 