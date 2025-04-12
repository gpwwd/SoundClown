package com.soundclown.auth.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-auth.properties")
public class AuthModuleConfig {
}
