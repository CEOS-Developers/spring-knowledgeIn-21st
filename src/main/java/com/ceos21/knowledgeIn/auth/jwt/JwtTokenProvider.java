package com.ceos21.knowledgeIn.auth.jwt;


import com.ceos21.knowledgeIn.exception.CustomJisikInException;
import com.ceos21.knowledgeIn.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/*
 * JWT 토큰의 생성, 검증, 그리고 토큰에서 사용자 정보를 추출
 * */

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final Key secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret()); // 인코딩된 key를 사용했으므로 디코딩 필요
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);  // 원본 시크릿을 Key객체로 변환
    }

    /**
     *  Access Token 생성
     */
    public String generateAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // 사용자 식별자
                .setIssuedAt(now)                     // 발급 시간
                .setExpiration(expiryDate)            // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 서명 알고리즘
                .compact();
    }

    /**
     *  토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token Expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("❌ Unsupported JWT");
        } catch (MalformedJwtException e) {
            System.out.println("❌ Malformed JWT");
        } catch (SecurityException | IllegalArgumentException e) {
            System.out.println("❌ Invalid JWT");
        }
        return false;
    }

    /**
     *  (살아있는 OR 만료된)토큰에서 사용자 ID 추출
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            return Long.parseLong(e.getClaims().getSubject());
        } catch (Exception e) {
            throw new CustomJisikInException(ErrorCode.INVALID_TOKEN);
        }
    }


    /**
     * RefreshToken 생성
    * */
    public String generateRefreshToken(Long userId, long expirationSeconds) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationSeconds * 1000);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getRemainExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}