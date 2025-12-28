// package com.example.demo.config;

// import com.example.demo.entity.Role;
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;

// import javax.crypto.SecretKey;
// import java.util.Date;

// public class JwtTokenProvider {

//     private final SecretKey secretKey;
//     private final long validityInMs;

//     public JwtTokenProvider(String secret, long validityInMs) {
//         this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
//         this.validityInMs = validityInMs;
//     }

//     public String generateToken(Long userId, String email, Role role) {
//         Date now = new Date();
//         Date expiry = new Date(now.getTime() + validityInMs);

//         return Jwts.builder()
//                 .setSubject(userId.toString())
//                 .claim("email", email)
//                 .claim("role", role.name())
//                 .setIssuedAt(now)
//                 .setExpiration(expiry)
//                 .signWith(secretKey)
//                 .compact();
//     }

//     public boolean validateToken(String token) {
//         try {
//             getClaims(token);
//             return true;
//         } catch (Exception e) {
//             return false;
//         }
//     }

//     public Claims getClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(secretKey)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
// }