package com.soundclown.storage.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:application-storage.properties")
@EntityScan(basePackages = "com.soundclown.storage.domain")
@EnableJpaRepositories(basePackages = "com.soundclown.storage.application.repository")
public class StorageModuleConfig {
}