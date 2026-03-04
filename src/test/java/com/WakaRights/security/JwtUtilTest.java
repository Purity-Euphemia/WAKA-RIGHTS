package com.WakaRights.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtUtilTest {

    JwtUtil jwtUtil = new JwtUtil();

    @Test
    void shouldGenerateToken() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("user@test.com");
        assertNotNull(token);
        assertEquals("user@test.com", jwtUtil.extractEmail(token));
    }
    @Test
    void
}