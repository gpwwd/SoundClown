package com.soundclown.auth.infrastructure.security.service;

import com.soundclown.auth.application.contracts.security.JwtService;
import com.soundclown.auth.application.dto.contract.JwtTokenData;
import com.soundclown.auth.infrastructure.security.user.SoundClownUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.token.expiry.hours}")
    private Long expiryHours;

    private final String secretKey;

    public JwtServiceImpl() {
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(SoundClownUserDetails userDetails) {
        var tokenData = new JwtTokenData(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return generateToken(tokenData);
    }

    @Override
    public String generateToken(JwtTokenData tokenData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", tokenData.authorities());

        var oneHourInMilliseconds = 1000 * 60 * 60;
        var now = new Date(System.currentTimeMillis());
        var expiryDate = new Date(System.currentTimeMillis() + oneHourInMilliseconds * expiryHours);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(tokenData.username())
                .issuedAt(now)
                .expiration(expiryDate)
                .and()
                .signWith(getKey())
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUserName(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
