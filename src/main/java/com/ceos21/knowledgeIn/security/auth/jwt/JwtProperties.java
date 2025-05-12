package com.ceos21.knowledgeIn.security.auth.jwt;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

/* JWT 설정
*
* */
// 스프링빈 등록: @EnableConfigurationProperties
// 환경변수 바인딩: @ConfigurationProperties
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")    //@EnableConfigurationPropeties로 스프링빈 등록
public class JwtProperties {

    private final String secret;
//    @Value("${jwt.access-token-expiration}")  => @ConfigurationProperties가 자동 바인딩
    private final long accessTokenExpiration;
//    @Value("${jwt.access-token-expiration}")
    private final long refreshTokenExpiration;

}