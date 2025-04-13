package com.soundclown.auth.application.service;

import com.soundclown.auth.application.contracts.security.JwtService;
import com.soundclown.auth.application.dto.contract.JwtTokenData;
import com.soundclown.auth.application.dto.request.LoginRequest;
import com.soundclown.auth.application.dto.response.AuthResponse;
import com.soundclown.auth.application.usecase.auth.LoginUseCase;
import com.soundclown.auth.infrastructure.security.user.SoundClownUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        var username = request.username();
        var password = request.password();

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        var token = jwtService.generateToken(
                new JwtTokenData(username, authentication.getAuthorities().stream()
                        .map(Objects::toString)
                        .toList()));

        var userDetails = (SoundClownUserDetails) authentication.getPrincipal();

        var authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .toList();

        return new AuthResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhoneNumber(),
                token,
                authorities
        );
    }
}
