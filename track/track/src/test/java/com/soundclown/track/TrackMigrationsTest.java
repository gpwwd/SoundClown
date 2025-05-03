package com.soundclown.track;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    classes = TestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
@ActiveProfiles("test")
public class TrackMigrationsTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void containerStartsAndAcceptsConnection() {
        assert(postgresContainer.isRunning());
        System.out.println("PostgreSQL container is running at: " + postgresContainer.getJdbcUrl());
    }

    @Test
    void migrationsAppliedToTrackSchema() throws Exception {
        try (
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
            );
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'track' AND table_name = 'flyway_schema_history'"
            )
        ) {
            rs.next();
            int count = rs.getInt(1);
            assert(count == 1) : "Таблица flyway_schema_history не найдена в схеме track, миграции не применились";

            try (java.sql.ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) FROM track.flyway_schema_history")) {
                rs2.next();
                int appliedMigrations = rs2.getInt(1);
                assert(appliedMigrations > 0) : "Нет применённых миграций в track.flyway_schema_history";
                System.out.println("Applied migrations: " + appliedMigrations);
            }
            
            // Проверка созданных таблиц
            try (java.sql.ResultSet rs3 = stmt.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'track' AND table_name IN ('genres', 'artists', 'albums', 'songs', 'album_genre', 'artist_genre', 'song_genre')"
            )) {
                rs3.next();
                int tableCount = rs3.getInt(1);
                assert(tableCount == 7) : "Не все таблицы созданы в схеме track";
                System.out.println("Created tables count: " + tableCount);
            }
        }
    }
} 