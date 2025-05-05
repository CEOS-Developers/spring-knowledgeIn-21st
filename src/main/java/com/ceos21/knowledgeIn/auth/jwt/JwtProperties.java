package com.ceos21.knowledgeIn.auth.jwt;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/* JWT 설정
*
* */
// 스프링빈으로 등록이 필요한데 설정값은 수동으로 등록해야하므로,
// 수동 빈 등록해야할듯  => 멤버변수 값을 주입받아서 사용할 수 없을거라고 생각했었는데, Spring이 application.yml에서 가져다 주입해줌.!

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

}