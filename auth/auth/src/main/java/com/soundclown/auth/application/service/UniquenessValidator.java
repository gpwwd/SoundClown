package com.soundclown.auth.application.service;

import com.soundclown.auth.application.exception.AlreadyTakenException;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class UniquenessValidator {

    public  <T> void validateUniqueness(String fieldName, T value, Predicate<T> existsCheck) {
        if (existsCheck.test(value)) {
            throw new AlreadyTakenException(fieldName, value.toString());
        }
    }
}
