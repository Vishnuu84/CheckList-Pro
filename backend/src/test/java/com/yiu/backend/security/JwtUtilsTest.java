package com.yiu.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String testSecret = "my-super-long-secret-key-that-is-at-least-32-characters";

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        // Manually inject the secret so it's not null during unit tests
        ReflectionTestUtils.setField(jwtUtils, "secret", testSecret);
    }

    @Test
    public void testTokenLifecycle() {
        String username = "test_dev";

        // 1. Generate
        String token = jwtUtils.generateToken(username);
        assertThat(token).isNotNull();

        // 2. Extract
        String extracted = jwtUtils.extractUsername(token);
        assertThat(extracted).isEqualTo(username);

        // 3. Validate
        boolean isValid = jwtUtils.isTokenValid(token, username);
        assertThat(isValid).isTrue();
    }

    @Test
    public void testExpiredOrInvalidToken() {
        String invalidToken = "not.a.real.token";

        // We expect the code to throw a RuntimeException for a malformed token
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
                    jwtUtils.isTokenValid(invalidToken, "anyUser");
                }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid or expired JWT token");
    }
}