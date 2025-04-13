package com.soundclown.auth.application.usecase;

import com.soundclown.auth.application.dto.request.ClientRegisterEmailRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;

public interface ClientRegisterUseCase{
    AuthResponse registerWithEmail(ClientRegisterEmailRequest request);
    //AuthResponse registerWithPhoneNumber(ClientRegisterPhoneNumberRequest request);
}
