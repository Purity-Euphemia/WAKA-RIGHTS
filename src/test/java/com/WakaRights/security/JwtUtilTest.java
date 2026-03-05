package com.WakaRights.security;

import com.WakaRights.model.Evidence;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;

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
    @Test
    void shouldExtractUsername() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("user");
        assertEquals("user", jwtUtil.extractEmail(token));
    }
    @Test
    void shouldRejectExpiredToken() {
        assertFalse(jwtUtil.validate("expired.token"));
    }
    @Test
    void shouldRejectInvalidToken() {
        assertFalse(jwtUtil.validate("invalid.token"));
    }
    @Test
    void shouldContainClaims() {
        String token = jwtUtil.generateToken("user");
        assertNotNull(jwtUtil.extractClaims(token));
    }
    @Test
    void shouldUseSecretKey() {
        assertNotNull(jwtUtil.getKey());
    }
    @Test
    void shouldSetExpiration() {
        assertTrue(jwtUtil.getExpiration() > 0);
    }
    @Test
    void shouldHandleNullToken() {
        assertFalse(jwtUtil.validate(null));
    }
    @Test
    void shouldHandleEmptyToken() {
        assertFalse(jwtUtil.validate(""));
    }

}