package com.ceos21.spring_knowledgeIn_21st.global.config;

import com.ceos21.spring_knowledgeIn_21st.global.jwt.*;
import com.ceos21.spring_knowledgeIn_21st.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {  // 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public RequestBodyCachingFilter requestBodyCachingFilter() {
        return new RequestBodyCachingFilter();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtUtil, refreshTokenRepository);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/signin");

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // answers : 게시글 내 답변 추가/조회
                        .requestMatchers(HttpMethod.GET, "/posts/*/answers").permitAll()
                        .requestMatchers(HttpMethod.POST, "/posts/*/answers").authenticated()

                        // Comments
                        .requestMatchers(HttpMethod.GET, "/posts/*/comments").permitAll()
                        .requestMatchers(HttpMethod.POST, "/posts/*/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/comments/**").authenticated()

                        // answers : 답변 단건 수정/삭제/조회
                        .requestMatchers(HttpMethod.GET, "/answers/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/answers/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/answers/**").authenticated()

                        // posts
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/posts/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/posts/**").authenticated()



                        .requestMatchers("/",
                                "/index.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/api/auth/signup",
                                "/api/auth/signin",
                                "/api/auth/refresh"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // 1) 캐싱 필터를 인증 필터(UsernamePasswordAuthenticationFilter) 앞에 등록
                .addFilterBefore(
                        requestBodyCachingFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                // 2) JWT 로그인 처리 필터 등록
               .addFilterBefore(
                       jwtAuthenticationFilter,
                       UsernamePasswordAuthenticationFilter.class
               )

                // 3) JWT 인가(Authorization) 필터 등록은 인증 필터 이후에
                .addFilterAfter(
                        new JwtAuthorizationFilter(jwtUtil, userDetailsService),
                        JwtAuthenticationFilter.class
                );

        return http.build();
    }
}
