package com.soundclown.auth.infrastructure.security.configuration;

import com.soundclown.auth.application.security.util.NoOpPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Value("${password.encoder.strength}")
    private int encoderStrength;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if ("dev".equalsIgnoreCase(activeProfile)) {
            return new NoOpPasswordEncoder();
        } else {
            return new BCryptPasswordEncoder(encoderStrength);
        }
    }
}