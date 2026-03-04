package com.WakaRights.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void shouldGenerateAndValidateToken() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("user@test.com");

        assertNotNull(token);
        assertTrue(jwtUtil.validate(token));
        assertEquals("user@test.com", jwtUtil.extractEmail(token));
    }
}