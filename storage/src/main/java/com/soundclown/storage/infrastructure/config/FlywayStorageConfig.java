package com.soundclown.storage.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayStorageConfig {

    @Bean
    public FluentConfiguration flywayStorageConfiguration(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas("storage")
                .table("flyway_history_storage")
                .locations("classpath:db/migration/storage");
    }
}
