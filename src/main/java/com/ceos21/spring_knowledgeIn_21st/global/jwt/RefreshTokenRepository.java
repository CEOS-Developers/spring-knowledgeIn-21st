package com.ceos21.spring_knowledgeIn_21st.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    private final long REFRESH_TOEKN_TTL = 1000 * 60 * 60 * 24 * 7L; // 7일

    public void save(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
                "refresh:" + email,
                refreshToken, REFRESH_TOEKN_TTL,
                TimeUnit.MILLISECONDS
        );
    }

    public String get(String email) {
        return (String) redisTemplate.opsForValue().get("refresh:" + email);
    }

    public void delete(String email) {
        redisTemplate.delete("refresh:" + email);
    }
}
