package com.soundclown.auth.application.dto.request.register.client;

import jakarta.validation.constraints.NotNull;

public record ClientRegisterPhoneNumberRequest(
        @NotNull String username,
        @NotNull String phoneNumber,
        @NotNull String password
) {
}
