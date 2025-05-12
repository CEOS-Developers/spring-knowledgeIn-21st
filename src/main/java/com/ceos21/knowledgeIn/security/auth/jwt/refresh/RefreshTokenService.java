package com.ceos21.knowledgeIn.security.auth.jwt.refresh;

import com.ceos21.knowledgeIn.security.auth.jwt.JwtProperties;
import com.ceos21.knowledgeIn.security.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;
    private static final String PREFIX = "RefreshToken:"; // 식별자 prefix

    public String saveToken(Long userId, String refreshToken) {
//        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, expirationSeconds); // 토큰의 만료기간

        redisTemplate.opsForValue().set(
                PREFIX + userId, refreshToken, Duration.ofSeconds(jwtProperties.getRefreshTokenExpiration() * 2)    // Redis에 저장되는 만료기간
        );

        return refreshToken;
    }

    public String getToken(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void deleteToken(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }


}
