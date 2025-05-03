package com.soundclown.track.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Duration {
    
    @Getter
    private int seconds;
    
    public Duration(int seconds) {
        validate(seconds);
        this.seconds = seconds;
    }
    
    private void validate(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }
    
    public String formatMinutesSeconds() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
    
    @Override
    public String toString() {
        return formatMinutesSeconds();
    }
} 