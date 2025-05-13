package com.soundclown.storage.domain.exception;

public class InvalidSongStateException extends RuntimeException {
    public InvalidSongStateException(String message) {
        super(message);
    }
} 