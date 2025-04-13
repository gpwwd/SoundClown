package com.soundclown.auth.application.security.util;

import com.soundclown.auth.domain.contracts.PasswordHasher;
import com.soundclown.auth.domain.valueobject.RawPassword;
import org.springframework.security.crypto.password.PasswordEncoder;

public class NoOpPasswordEncoder implements PasswordEncoder, PasswordHasher {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var rawPasswordString = rawPassword.toString();
        return rawPasswordString.equals(encodedPassword);
    }

    @Override
    public String hashPassword(RawPassword rawPassword) {
        return encode(rawPassword.value());
    }
}