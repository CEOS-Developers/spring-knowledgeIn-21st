package com.ceos21.knowledgeIn.security.auth.jwt;


import com.ceos21.knowledgeIn.exception.CustomJisikInException;
import com.ceos21.knowledgeIn.exception.ErrorCode;
import com.ceos21.knowledgeIn.security.auth.jwt.refresh.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/*
 * JWT 토큰의 생성, 검증, 그리고 토큰에서 사용자 정보를 추출
 * */

@Slf4j
@Getter
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final Key secretKey;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public JwtTokenProvider(JwtProperties jwtProperties, RefreshTokenService refreshTokenService) {
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret()); // 인코딩된 key를 사용했으므로 디코딩 필요
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);  // 원본 시크릿을 Key객체로 변환
        this.refreshTokenService = refreshTokenService;
    }

    /**
     *  Access Token 생성
     */
    public String generateAccessToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // subject로 username이 아니라 userId 사용
                .claim("username", username)
                .setIssuedAt(now)                     // 발급 시간
                .setExpiration(expiryDate)            // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 서명 알고리즘
                .compact();
    }

    /**
     *  토큰 유효성 검증
     *  유효: true
     *  만료: false
     *  그외 토큰 오류: exception
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            throw new CustomJisikInException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            throw new CustomJisikInException(ErrorCode.MALFORMED_TOKEN);
        } catch (SecurityException | IllegalArgumentException e) {
            throw new CustomJisikInException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     *  (살아있는 OR 만료된)토큰에서 사용자 ID 추출
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            return Long.parseLong(e.getClaims().getSubject());
        } catch (Exception e) {
            throw new CustomJisikInException(ErrorCode.INVALID_TOKEN);
        }
    }


    /**
     * (살아있는 OR 만료된)토큰에서 username 추출
    * */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.get("username", String.class);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("username", String.class);
        } catch (Exception e) {
            throw new CustomJisikInException(ErrorCode.INVALID_TOKEN);
        }
    }


    /**
     * RefreshToken 생성
     * TODO RefreshToken 클래스를 사용하지 않고있음. 개선사항
    * */
    public String generateRefreshToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getRemainExpiration(String token) {
        Claims claims = parseClaims(token);

        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    private Claims parseClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    /**
     * 본인 refresh token인지 검증
     * */
    public void validateTokenOwnership(String accessToken, String refreshToken) {

        Long userId = getUserIdFromToken(accessToken);
        String savedRefreshToken = refreshTokenService.getToken(userId);

        if (!refreshToken.equals(savedRefreshToken)) {
            throw new CustomJisikInException(ErrorCode.TOKEN_MISMATCH);
        }

    }

}