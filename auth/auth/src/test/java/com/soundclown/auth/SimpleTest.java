package com.soundclown.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SimpleTest {

    @Test
    void simpleTest() {
        // Простой тест, который не требует контекста Spring
        assert(true);
    }
} 