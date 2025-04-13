package com.soundclown.auth.application.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AuthResponse(
        @NotNull Long id,
        @NotNull String username,
        String email,
        String phoneNumber,
        @NotNull String token,
        @NotNull List<String> authorities
) {}
