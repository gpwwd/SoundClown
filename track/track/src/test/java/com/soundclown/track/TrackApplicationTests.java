package com.soundclown.track;

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
