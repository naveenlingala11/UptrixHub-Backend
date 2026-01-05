package com.ja.security;

import com.ja.user.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRY = 1000 * 60 * 60 * 24; // 24 hours

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ FIXED METHOD (Long + Role)
    public String generateToken(Long userId, Role role) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // userId as subject
                .claim("role", role.name())            // role claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ REQUIRED
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

// JwtUtil.java

    public String generateImpersonationToken(Long userId, Long adminId) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", "USER")
                .claim("impersonatedBy", adminId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractImpersonatedBy(String token) {
        Claims claims = extractAllClaims(token);
        Object v = claims.get("impersonatedBy");
        return v == null ? null : Long.parseLong(v.toString());
    }

}
