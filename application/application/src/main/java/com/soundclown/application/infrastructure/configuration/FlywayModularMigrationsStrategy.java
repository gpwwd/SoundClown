package com.soundclown.application.infrastructure.configuration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A custom {@link FlywayMigrationStrategy} implementation that enables modular and isolated
 * Flyway migrations across multiple schemas with separate migration histories.
 * <p>
 * This strategy receives a list of {@link FluentConfiguration} instances provided by
 * different feature modules (e.g., auth, catalog), each representing a unique Flyway configuration.
 * This allows each module to define and manage its own schema evolution in isolation from others.
 *
 */
@Component
public class FlywayModularMigrationsStrategy implements FlywayMigrationStrategy {

    private final List<FluentConfiguration> configurations;

    public FlywayModularMigrationsStrategy(List<FluentConfiguration> configurations) {
        this.configurations = configurations;
    }

    @Override
    public void migrate(Flyway flyway) {

        for (var configuration : configurations) {
            var migration = configuration.load();

            migration.migrate();
        }
    }
}
