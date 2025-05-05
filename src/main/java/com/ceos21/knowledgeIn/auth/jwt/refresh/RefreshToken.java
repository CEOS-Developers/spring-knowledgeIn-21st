package com.ceos21.knowledgeIn.auth.jwt.refresh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor  //필수
@RedisHash(value = "refreshToken")   // , timeToLive = 60*60*24 TTL: s단위, 24시간
public class RefreshToken {
    @Id // JPA의 @Id쓰면 안됨!!
    private String userId;  // redis는 (key,value)저장소이기에 key를 수동 등록, String인게 자연스러움
    private String token;
}
