package com.soundclown.auth.application.dto.request.register.admin;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AdminRegisterEmailRequest(
        @NotNull String username,
        @NotNull String email,
        @NotNull String password,
        @NotNull String role
) {}