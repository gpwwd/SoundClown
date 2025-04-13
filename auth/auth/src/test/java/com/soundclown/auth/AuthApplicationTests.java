package com.soundclown.auth;

import com.soundclown.auth.domain.valueobject.Username;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        Username username = new Username("e");
    }

    @SpringBootApplication
    static class TestConfiguration {
    }

}
