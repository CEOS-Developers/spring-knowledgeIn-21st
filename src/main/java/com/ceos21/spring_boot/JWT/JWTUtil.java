package com.ceos21.spring_boot.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 생성, 파싱/검증
@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JWTUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${spring.jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs
    ) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    // 서명 검증 (예외 던짐)
    public void verifyToken(String token) {
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    // 공통 Claims 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 필드 추출
    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public String getCategory(String token) {
        return getClaims(token).get("category", String.class);
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // 토큰 생성
    public String createJwt(String type, String email, String role) {
        long now = System.currentTimeMillis();
        long expiredMs = type.equals("access") ? accessTokenExpirationMs : refreshTokenExpirationMs;

        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .setExpiration(new Date(now + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}