package com.ceos21.springknowledgein.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key key;
    private final long EXPIRATION_TIME = 60 * 60 * 1000L;

    @PostConstruct //빈 초기화 시점에 해당 메서드가 실행
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);//secret.key 디코딩하여 원래 바이트 배열로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes);//이 키가 JWT 서명에 필요한 키가 됨.
    }

    public String createAccessToken(String username, String role) {
        Date now = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("서명이 이상합니당");
        } catch (ExpiredJwtException e) {
            log.error("만료됨");
        } catch(UnsupportedJwtException e) {
            log.error("노 지원");
        } catch(IllegalArgumentException e) {
            log.error("JWT claims가 비었습니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}
