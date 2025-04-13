package com.soundclown.auth.application.usecase.auth;

import com.soundclown.auth.application.dto.request.register.client.ClientRegisterEmailRequest;
import com.soundclown.auth.application.dto.request.register.client.ClientRegisterPhoneNumberRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;

public interface ClientRegisterUseCase{
    AuthResponse registerWithEmail(ClientRegisterEmailRequest request);
    AuthResponse registerWithPhoneNumber(ClientRegisterPhoneNumberRequest request);
}
