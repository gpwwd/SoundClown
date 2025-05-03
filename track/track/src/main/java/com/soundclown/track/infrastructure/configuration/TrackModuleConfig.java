package com.soundclown.track.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@PropertySource("classpath:application-track.properties")
@EnableJpaRepositories(basePackages = "com.soundclown.track.application.repository")
@EntityScan(basePackages = "com.soundclown.track.domain")
public class TrackModuleConfig {
}
