package com.soundclown.auth.application.dto.request.register.admin;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AdminRegisterPhoneNumberRequest(
        @NotNull String username,
        @NotNull String phoneNumber,
        @NotNull String password,
        @NotNull String role
) {
}