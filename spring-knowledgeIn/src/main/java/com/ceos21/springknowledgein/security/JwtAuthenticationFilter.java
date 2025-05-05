package com.ceos21.springknowledgein.security;

import com.ceos21.springknowledgein.domain.user.dto.LoginRequestDto;
import com.ceos21.springknowledgein.security.user.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;

        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/api/user/login", "POST")
        );
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequestDto loginRequest = objectMapper.readValue(
                    request.getInputStream(), LoginRequestDto.class
            );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            return this.getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(
                userDetails.getUsername(),
                userDetails.getMember().getRole().name()
        );

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
