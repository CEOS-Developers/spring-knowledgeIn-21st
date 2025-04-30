package com.ceos21.knowledgein.security.filter;

import com.ceos21.knowledgein.security.exception.SecurityErrorCode;
import com.ceos21.knowledgein.security.exception.TokenException;
import com.ceos21.knowledgein.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.ceos21.knowledgein.security.exception.SecurityErrorCode.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access");

        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Access token from request: {}", accessToken);

        validate(response, accessToken);
        setAuthentication(accessToken);
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletResponse response, String accessToken) {
        // access 만료 - refresh token 도입 시 재발급 로직 여기에 추가
        if (!jwtProvider.validateToken(accessToken)) {
            throw new TokenException(ACCESS_EXPIRED);
        }
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
