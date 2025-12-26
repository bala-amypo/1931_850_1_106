package com.example.demo.config;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * Configuration package wrapper for JwtTokenProvider.
 * This delegates all calls to the security package implementation.
 */
@Component
public class ConfigJwtTokenProvider {
    
    private final com.example.demo.security.JwtTokenProvider jwtTokenProvider;
    
    public ConfigJwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis) {
        this.jwtTokenProvider = new com.example.demo.security.JwtTokenProvider(secret, expirationMillis);
    }
    
    public String generateToken(Long userId, String email, String role) {
        return jwtTokenProvider.generateToken(userId, email, role);
    }
    
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
    
    public Claims getClaims(String token) {
        return jwtTokenProvider.getClaims(token);
    }
    
    public String extractEmail(String token) {
        return jwtTokenProvider.extractEmail(token);
    }
    
    public Long extractUserId(String token) {
        return jwtTokenProvider.extractUserId(token);
    }
    
    public String extractRole(String token) {
        return jwtTokenProvider.extractRole(token);
    }
}
