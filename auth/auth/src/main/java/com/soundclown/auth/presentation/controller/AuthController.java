package com.soundclown.auth.presentation.controller;

import com.soundclown.auth.application.dto.request.ClientRegisterEmailRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;
import com.soundclown.auth.application.usecase.ClientRegisterUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClientRegisterUseCase clientRegisterUseCase;

    @Autowired
    public AuthController(ClientRegisterUseCase clientRegisterUseCase) {
        this.clientRegisterUseCase = clientRegisterUseCase;
    }

    @PostMapping("/register/email")
    public ResponseEntity<AuthResponse> registerClientEmail(@RequestBody ClientRegisterEmailRequest request) {
        return ResponseEntity.ok(
                clientRegisterUseCase.registerWithEmail(request));
    }
}
