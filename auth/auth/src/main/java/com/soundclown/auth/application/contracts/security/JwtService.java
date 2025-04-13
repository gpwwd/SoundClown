package com.soundclown.auth.application.contracts.security;

import com.soundclown.auth.application.dto.contract.JwtTokenData;

public interface JwtService {
    String generateToken(JwtTokenData tokenData);
    boolean validateToken(String token, String username);
    String extractUserName(String token);
}
