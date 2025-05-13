package com.soundclown.storage.presentation.exception;

import com.soundclown.storage.domain.exception.InvalidSongStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StorageExceptionHandler {

    @ExceptionHandler(InvalidSongStateException.class)
    public ResponseEntity<String> handleInvalidSongState(InvalidSongStateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
} 