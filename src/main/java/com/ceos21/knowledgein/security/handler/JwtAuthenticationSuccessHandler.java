package com.ceos21.knowledgein.security.handler;

import com.ceos21.knowledgein.redis.service.RefreshTokenRedisService;
import com.ceos21.knowledgein.security.dto.PrincipalUserDetails;
import com.ceos21.knowledgein.security.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;
    @Value("${jwt.expiration.refresh}")
    private Long refreshTokenExpiration;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String userId = findUserIdFromAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication, userId);
        String refreshToken = jwtProvider.generateRefreshToken(authentication, userId);

        refreshTokenRedisService.saveRefreshToken(Long.parseLong(userId), refreshToken, refreshTokenExpiration);

        response.setHeader("access", accessToken);
    }

    private String findUserIdFromAuthentication(Authentication authentication) {
        PrincipalUserDetails principal = (PrincipalUserDetails) authentication.getPrincipal();
        return principal.getUserEntity().getId().toString();
    }
}
