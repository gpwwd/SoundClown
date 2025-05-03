package com.soundclown.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringJUnitConfig(TestcontainersExampleTest.TestConfig.class)
@Testcontainers
@ActiveProfiles("test")
public class TestcontainersExampleTest {

    // Объявляем контейнер PostgreSQL
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    // Динамически настраиваем подключение к тестовому контейнеру
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
    
    @Test
    void containerStartsAndAcceptsConnection() {
        // Проверяем, что контейнер запустился и принимает соединения
        assert(postgresContainer.isRunning());
        System.out.println("PostgreSQL container is running at: " + postgresContainer.getJdbcUrl());
    }

    /**
     * Проверяет, что миграции из resources/db/migration/auth применились к тестовой БД.
     * Для этого проверяет наличие таблицы, которую должна создать миграция, или записи в flyway_schema_history.
     */
    @Test
    void migrationsAppliedToTestDatabase() throws Exception {
        // Попробуем подключиться к БД и проверить наличие таблицы flyway_schema_history
        try (
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
            );
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'flyway_schema_history'"
            )
        ) {
            rs.next();
            int count = rs.getInt(1);
            assert(count == 1) : "Таблица flyway_schema_history не найдена, миграции не применились";

            // Проверим, что хотя бы одна миграция применена (есть запись в flyway_schema_history)
            try (java.sql.ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM flyway_schema_history")) {
                rs2.next();
                int appliedMigrations = rs2.getInt(1);
                assert(appliedMigrations > 0) : "Нет применённых миграций в flyway_schema_history";
                System.out.println("Applied migrations: " + appliedMigrations);
            }
        }
    }

    @AutoConfiguration
    static class TestConfig {
        // Пустая конфигурация для теста
    }
} 