package com.ceos21.knowledgeIn.auth.jwt.refresh;

import com.ceos21.knowledgeIn.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String PREFIX = "RefreshToken:"; // 식별자 prefix

    public String createToken(Long userId, long expirationSeconds) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, expirationSeconds); // 토큰의 만료기간

        redisTemplate.opsForValue().set(
                PREFIX + userId, refreshToken, Duration.ofSeconds(expirationSeconds * 2)    // Redis에 저장되는 만료기간
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
