package com.mohithasan.journalapp.utilis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTests {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();
        Field secretKeyField = JwtUtil.class.getDeclaredField("secreteKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtUtil, "my-test-secret-key-my-test-secret-key");
    }

    @Test
    void shouldGenerateAndExtractUsername() {
        String token = jwtUtil.generateToken("mohith");
        assertNotNull(token);
        String username = jwtUtil.extractUsername(token);
        assertEquals("mohith", username);
    }

    @Test
    void shouldValidateToken() {
        String token = jwtUtil.generateToken("user1");
        Boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }
}
