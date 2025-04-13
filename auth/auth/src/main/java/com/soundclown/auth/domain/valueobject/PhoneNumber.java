package com.soundclown.auth.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record PhoneNumber(
        @Column(name = "phone_number", nullable = false, unique = true)
        String value
) {
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{9,15}$");

    public PhoneNumber(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid phone number format: " + value);
        }
        this.value = value;
    }

    private boolean isValid(String value) {
        return value != null && PHONE_PATTERN.matcher(value).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PhoneNumber other = (PhoneNumber) o;
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
    protected PhoneNumber() {
        this(null);
    }
}
