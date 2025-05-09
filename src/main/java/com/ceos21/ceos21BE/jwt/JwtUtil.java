package com.ceos21.ceos21BE.jwt;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;
import com.ceos21.ceos21BE.customDetail.CustomDetails;
import com.ceos21.ceos21BE.customDetail.CustomDetailsService;
import com.ceos21.ceos21BE.jwt.dto.JwtDto;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long access;
    private final Long refreshTokenExpiration;
    private final CustomDetailsService customDetailsService;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-token-expiration}") Long access,
                   @Value("${jwt.refresh-token-expiration}") Long refreshTokenExpiration,
                   CustomDetailsService customDetailsService) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.access = access;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.customDetailsService = customDetailsService;
    }

    // JWT 토큰 입력으로 받아 토큰의 페이로드에서 사용자 이름 추출
    public String getUsername(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRoles(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", String.class);
    }

    public long getExpirationTime(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime();
    }

    public String tokenProvider(CustomDetails principalDetails, Instant expiration) {
        Instant issuedAt = Instant.now();
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(principalDetails.getUsername())
                .claim("roles", authorities)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtAccessToken(CustomDetails principalDetails) {
        Instant expiration = Instant.now().plusMillis(access);
        return tokenProvider(principalDetails, expiration);
    }

    public String createJwtRefreshToken(CustomDetails principalDetails) {
        Instant expiration = Instant.now().plusMillis(refreshTokenExpiration);
        String refreshToken = tokenProvider(principalDetails, expiration);

        return tokenProvider(principalDetails, expiration);
    }

    // HTTP 요ㅓㅇ 시 Authorization header에서 JWT token 검색
    public String resolveAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("[*] JWT Token not found");
            return null;
        }
        log.info("[*] JWT Token found");

        return authorizationHeader.split(" ")[1];
    }

    // token validation test
    public void validationToken(String token) {
        try {
            // Jwt 만료 시간 검증 시 클라이언트와 서버 시간 차이 고려
            long second = 3 * 60;

            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(second)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());

            log.info("Authorization with Token");

            if(isExpired) {
                log.info("[*] Token is Expired");
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Token is Invalid");
        } catch (ExpiredJwtException e) {
            log.info("Token is Expired");
        } catch (UnsupportedJwtException e) {
            log.info("Token is Unsupported");
        } catch (IllegalArgumentException e) {
            log.info("Token is Illegal");
        }
    }

    public boolean validateRefreshToken(String refreshToken) throws SignatureException {
        String username = getUsername(refreshToken);

        try {
            // 유효성 검사 & 만료 유무
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(refreshToken);

            return true;
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._TOKEN_INVALID);
        }
    }

    public JwtDto reissueToken(String refreshToken) throws SignatureException {
        UserDetails userDetails = customDetailsService.loadUserByUsername(getUsername(refreshToken));

        return new JwtDto(
                createJwtAccessToken((CustomDetails) userDetails),
                createJwtRefreshToken((CustomDetails) userDetails)
        );
    }

    public String createAccessToken(String email, String role) {
        Instant expiration = Instant.now().plusMillis(access);

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .subject(email)
                .claim("roles", role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }
}
