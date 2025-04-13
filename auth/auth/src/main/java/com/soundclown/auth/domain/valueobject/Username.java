package com.soundclown.auth.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record Username(
        @Column(name = "username", nullable = false, unique = true)
        String value
) {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^(?!.*[_.]{2})[A-Za-z0-9][A-Za-z0-9_.]{1,50}[A-Za-z0-9]$");

    public Username(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid username format: " + value);
        }
        this.value = value;
    }

    private boolean isValid(String value) {
        return value != null && USERNAME_PATTERN.matcher(value).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username other = (Username) o;
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
    protected Username() {
        this(null);
    }
}