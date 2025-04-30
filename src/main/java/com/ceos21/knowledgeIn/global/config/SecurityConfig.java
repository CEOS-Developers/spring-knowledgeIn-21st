package com.ceos21.knowledgeIn.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /**
         * jwt를 사용하기 때문에 아래의 인증방식은 disable 처리 해준다.
         */
        http.csrf((auth)->auth.disable());
        http.formLogin((auth)->auth.disable());
        http.httpBasic((auth)->auth.disable());

        //경로 권한 설정
        http.authorizeHttpRequests((auth)->auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/").hasRole("ADMIN")
                .requestMatchers("/").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
        );

        //세션 설정: 무상태로
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
}
