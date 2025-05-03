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
public class Description {
    
    @Getter
    private String value;
    
    public Description(String value) {
        this.value = value; // Может быть null или пустым
    }
    
    @Override
    public String toString() {
        return value != null ? value : "";
    }
} 