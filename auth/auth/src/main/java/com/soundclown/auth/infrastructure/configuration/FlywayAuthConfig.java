package com.soundclown.auth.infrastructure.configuration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayAuthConfig {

    @Bean
    public FluentConfiguration flywayAuthConfiguration(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas("auth")
                .table("flyway_history_auth")
                .locations("classpath:db/migration/auth");
    }
}
