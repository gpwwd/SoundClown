package com.soundclown.auth.presentation.controller;

import com.soundclown.auth.application.dto.request.register.admin.AdminRegisterEmailRequest;
import com.soundclown.auth.application.dto.request.register.admin.AdminRegisterPhoneNumberRequest;
import com.soundclown.auth.application.dto.request.register.client.ClientRegisterEmailRequest;
import com.soundclown.auth.application.dto.request.register.client.ClientRegisterPhoneNumberRequest;
import com.soundclown.auth.application.dto.request.LoginRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;
import com.soundclown.auth.application.usecase.auth.AdminRegisterUseCase;
import com.soundclown.auth.application.usecase.auth.ClientRegisterUseCase;
import com.soundclown.auth.application.usecase.auth.LoginUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClientRegisterUseCase clientRegisterUseCase;
    private final AdminRegisterUseCase adminRegisterUseCase;
    private final LoginUseCase loginUseCase;

    @Autowired
    public AuthController(ClientRegisterUseCase clientRegisterUseCase,
                          AdminRegisterUseCase adminRegisterUseCase, LoginUseCase loginUseCase) {
        this.clientRegisterUseCase = clientRegisterUseCase;
        this.adminRegisterUseCase = adminRegisterUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                loginUseCase.login(request));
    }

    @PostMapping("/register/email")
    public ResponseEntity<AuthResponse> registerClientEmail(@RequestBody ClientRegisterEmailRequest request) {
        return ResponseEntity.ok(
                clientRegisterUseCase.registerWithEmail(request));
    }

    @PostMapping("/register/phone-number")
    public ResponseEntity<AuthResponse> registerClientPhoneNumber(@RequestBody ClientRegisterPhoneNumberRequest request) {
        return ResponseEntity.ok(
                clientRegisterUseCase.registerWithPhoneNumber(request));
    }

    @PostMapping("/admins/register/email")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGER', 'ADMIN_GOD')")
    public ResponseEntity<AuthResponse> registerAdminEmail(@RequestBody AdminRegisterEmailRequest request) {
        return ResponseEntity.ok(
                adminRegisterUseCase.registerWithEmail(request));
    }

    @PostMapping("/admins/register/phone-number")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGER', 'ADMIN_GOD')")
    public ResponseEntity<AuthResponse> registerAdminPhoneNumber(@RequestBody AdminRegisterPhoneNumberRequest request) {
        return ResponseEntity.ok(
                adminRegisterUseCase.registerWithPhoneNumber(request));
    }
}
