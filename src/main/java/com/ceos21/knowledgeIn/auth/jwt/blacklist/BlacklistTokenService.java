package com.ceos21.knowledgeIn.auth.jwt.blacklist;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BlacklistTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "BlackList:";

    // accessToken을 블랙리스트로 등록
    public void blacklistAccessToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(
                PREFIX + token, "logout", Duration.ofMillis(expirationMillis)
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(PREFIX + token);
    }
}
