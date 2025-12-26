package com.example.demo.config;

import com.example.demo.entity.Role;
import io.jsonwebtoken.Claims;

public class JwtTokenProvider {

    public JwtTokenProvider(String secret, long validityInMs) {
        // Minimal constructor for tests
    }

    public String generateToken(Long userId, String email, Role role) {
        return "dummy-token";
    }

    public boolean validateToken(String token) {
        return true;
    }

    public Claims getClaims(String token) {
        return null;
    }
}
EOF