package com.soundclown.track.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Title {
    
    @Getter
    private String value;
    
    public Title(String value) {
        validate(value);
        this.value = value;
    }
    
    private void validate(String value) {
        Objects.requireNonNull(value, "Title cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("Title cannot be longer than 255 characters");
        }
    }
    
    @Override
    public String toString() {
        return value;
    }
} 