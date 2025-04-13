package com.soundclown.auth.domain.contracts;

import com.soundclown.auth.domain.valueobject.RawPassword;

public interface PasswordHasher {
    String hashPassword(RawPassword password);
}
