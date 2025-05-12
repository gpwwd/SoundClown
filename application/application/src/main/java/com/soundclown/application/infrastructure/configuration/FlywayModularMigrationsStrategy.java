package com.soundclown.application.infrastructure.configuration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import java.sql.SQLException;
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
    private final DataSource dataSource;

    public FlywayModularMigrationsStrategy(List<FluentConfiguration> configurations, DataSource dataSource) {
        this.configurations = configurations;
        this.dataSource = dataSource;
    }

    @Override
    public void migrate(Flyway flyway) {

        Set<String> schemas = configurations.stream()
                .flatMap(cfg -> Arrays.stream(cfg.getSchemas()))
                .collect(Collectors.toSet());

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String schema : schemas) {
                stmt.execute("CREATE SCHEMA IF NOT EXISTS " + schema);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create schemas", e);
        }


        for (var configuration : configurations) {
            configuration.baselineOnMigrate(true);
            var migration = configuration.load();

            migration.migrate();
        }
    }
}