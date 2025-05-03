package com.ceos21.spring_knowledgeIn_21st.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class RequestBodyCachingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        // 1) 원본 요청을 ContentCachingRequestWrapper로 감싼다
        ContentCachingRequestWrapper wrapped =
                new ContentCachingRequestWrapper(request);

        // 2) 체인 진행 (이후 JwtAuthenticationFilter가 이 래퍼를 받음)
        chain.doFilter(wrapped, response);
    }
}
