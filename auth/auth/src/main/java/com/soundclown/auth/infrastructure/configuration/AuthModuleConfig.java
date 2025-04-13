package com.soundclown.auth.infrastructure.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:application-auth.properties")
@EnableJpaRepositories(basePackages = "com.soundclown.auth.application.repository")
@EntityScan(basePackages = "com.soundclown.auth.domain.model")
public class AuthModuleConfig {
}
