package com.yiu.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    public void testTokenLifecycle() {
        String username = "security_test_user";

        // 1. Generate
        String token = jwtUtils.generateToken(username);
        assertThat(token).isNotBlank();

        // 2. Extract
        String extracted = jwtUtils.extractUsername(token);
        assertThat(extracted).isEqualTo(username);
    }
}