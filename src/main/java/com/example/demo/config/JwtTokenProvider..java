package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Alias for JwtTokenProvider to maintain backward compatibility with tests.
 * This class exists in the config package for test compatibility,
 * but delegates all functionality to the security package implementation.
 * 
 * The actual implementation is in com.example.demo.security.JwtTokenProvider
 */
@Component("configJwtTokenProvider")
public class JwtTokenProvider extends com.example.demo.security.JwtTokenProvider {

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis) {
        super(secret, expirationMillis);
    }
}
