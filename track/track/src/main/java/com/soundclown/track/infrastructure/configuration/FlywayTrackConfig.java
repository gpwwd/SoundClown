package com.soundclown.track.infrastructure.configuration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayTrackConfig {

    @Bean
    public FluentConfiguration flywayTrackConfiguration(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas("track")
                .table("flyway_history_track")
                .locations("classpath:db/migration/track");
    }
}
