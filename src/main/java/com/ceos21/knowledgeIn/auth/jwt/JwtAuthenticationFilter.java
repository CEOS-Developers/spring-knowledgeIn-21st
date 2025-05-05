package com.ceos21.knowledgeIn.auth.jwt;

import com.ceos21.knowledgeIn.auth.user.detail.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* 역할 정리
HTTP 요청의 Authorization 헤더에서 JWT 토큰 추출
토큰의 유효성 검증
유효하다면, Spring Security 인증 객체(SecurityContext) 에 등록
유효하지 않으면 인증 없이 다음 필터로 넘김 (혹은 예외 처리)
* */

// 개선사항: 실제로는 사용자 정보를 DB에서 조회하거나, UserDetails를 활용해 권한(Role)까지 넣는 게 일반적입니다.

// JWT 토큰 검증 및 인증처리
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더 Authorization 필드에서 토큰 추출
        String token = getTokenFromRequest(request);

        // token 검증: token이 유효하면
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userId.toString());
            // 인증 객체 생성: Principal로 UserDetails넣음
            // 나중에 UserDetails를 꺼낼 때: SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext 컨텍스트에 인증정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터에게 request 전달
        filterChain.doFilter(request, response);
    }

    // 헤더에서 "Authentication" 필드의 Bearer 토큰 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);    //"Bearer "이후 추출
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();

        return requestURI.startsWith("/api/v1/users/signup") || requestURI.equals("/api/v1/users/signin");
    }
}
