package com.soundclown.auth.application.usecase.auth;

import com.soundclown.auth.application.dto.request.register.admin.AdminRegisterEmailRequest;
import com.soundclown.auth.application.dto.request.register.admin.AdminRegisterPhoneNumberRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;

public interface AdminRegisterUseCase {
    AuthResponse registerWithPhoneNumber(AdminRegisterPhoneNumberRequest request);
    AuthResponse registerWithEmail(AdminRegisterEmailRequest request);
}