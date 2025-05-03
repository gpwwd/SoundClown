package com.soundclown.track;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.soundclown.track")
@EntityScan(basePackages = "com.soundclown.track.domain")
@EnableJpaRepositories(basePackages = "com.soundclown.track.application.repository")
public class TestConfiguration {
} 