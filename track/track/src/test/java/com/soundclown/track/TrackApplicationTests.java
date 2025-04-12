package com.soundclown.track;

import com.soundclown.track.infrastructure.configuration.TrackModuleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TrackApplicationTests {

    @Test
    void contextLoads() {
    }

    @SpringBootApplication
    static class TestConfiguration {
    }

}
