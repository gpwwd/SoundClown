package com.soundclown.auth.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record RawPassword(String value) {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[A-Za-z\\d\\W]{6,}$"); // At least 6 letters/digits

    public RawPassword {
        if (!isValid(value)) {
            throw new IllegalArgumentException(
                    "Password must be at least 6 characters long.");
        }
    }

    private boolean isValid(String value) {
        return value != null && PASSWORD_PATTERN.matcher(value).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawPassword other = (RawPassword) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * For JPA Generation
     */
    protected RawPassword() {
        this(null);
    }
}
