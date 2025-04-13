package com.soundclown.auth.application.usecase.auth;

import com.soundclown.auth.application.dto.request.LoginRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;

public interface LoginUseCase {
    AuthResponse login(LoginRequest loginRequest);
}
