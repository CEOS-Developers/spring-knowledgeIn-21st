package com.ceos21.knowledgeIn.global.config;

import com.ceos21.knowledgeIn.global.jwt.JwtAuthenticationFilter;
import com.ceos21.knowledgeIn.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /**
         * jwt를 사용하기 때문에 아래의 인증방식은 disable 처리 해준다.
         */
        http
                .csrf((auth)->auth.disable())
                .formLogin((auth)->auth.disable())
                .httpBasic((auth)->auth.disable());

        //경로 권한 설정
        http.authorizeHttpRequests((auth)->auth
                .requestMatchers("/api/members/signUp","api/members/signIn").permitAll()
                .requestMatchers(HttpMethod.POST,"/").hasAuthority("MEMBER")
                .requestMatchers("/").hasAnyAuthority("MEMBER", "ADMIN")
                .anyRequest().authenticated()
        );

        //세션 설정: 무상태로
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //jwt 인증을 위해 직접 구현한 jwtFilter를 UsernamePasswordAuthenticationFilter 전에 실행
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
