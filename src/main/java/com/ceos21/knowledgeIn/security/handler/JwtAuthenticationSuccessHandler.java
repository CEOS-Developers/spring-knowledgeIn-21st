package com.ceos21.knowledgeIn.security.handler;

import com.ceos21.knowledgeIn.security.auth.jwt.JwtTokenProvider;
import com.ceos21.knowledgeIn.security.auth.jwt.refresh.RefreshTokenService;
import com.ceos21.knowledgeIn.security.auth.user.detail.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(userDetails.getUserId(), userDetails.getUsername());
        String refreshToken = tokenProvider.generateRefreshToken(userDetails.getUserId(), userDetails.getUsername());

        refreshService.saveToken(userDetails.getUserId(), refreshToken);

        // accessToken 헤더 담기
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);

        //refershToken 쿠키 담기
        long cookieAge = tokenProvider.getJwtProperties().getRefreshTokenExpiration() / 1000;   // 초 단위
        String refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(cookieAge)
                .build()
                .toString();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie);

/**
 * response.setHeader(), addHeader() 차이
* 단일 값 헤더(Authorization, Content-Type 등)는 보통 setHeader
 * 멀티 값 헤더(Set-Cookie, Cache-Control 등)나 같은 이름으로 여러 값을 남기고 싶을 땐 addHeader
* */
        // 5) 상태 코드
        response.setStatus(HttpStatus.CREATED.value());
    }
}
