package com.soundclown.auth;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.soundclown.auth")
@EntityScan(basePackages = "com.soundclown.auth.domain")
@EnableJpaRepositories(basePackages = "com.soundclown.auth.application.repository")
public class TestConfiguration {
} 