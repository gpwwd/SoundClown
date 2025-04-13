package com.soundclown.auth.application.exception;

public class AlreadyTakenException extends RuntimeException {

    public AlreadyTakenException(String fieldName, String value) {
        super(fieldName + " is already taken: " + value);
    }

}

