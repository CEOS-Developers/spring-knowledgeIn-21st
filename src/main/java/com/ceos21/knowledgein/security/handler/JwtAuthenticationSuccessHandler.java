package com.ceos21.knowledgein.security.handler;

import com.ceos21.knowledgein.global.dto.CommonResponse;
import com.ceos21.knowledgein.redis.service.RefreshTokenRedisService;
import com.ceos21.knowledgein.security.dto.PrincipalUserDetails;
import com.ceos21.knowledgein.security.jwt.JwtProvider;
import com.ceos21.knowledgein.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String userId = findUserIdFromAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication, userId);
        String refreshToken = jwtProvider.generateRefreshToken(authentication, userId);

        refreshTokenRedisService.saveRefreshToken(Long.parseLong(userId), refreshToken, refreshTokenExpiration);

        response.setHeader("access", accessToken);
        writeBodyWithUserDto(response, authentication);
    }

    private String findUserIdFromAuthentication(Authentication authentication) {
        PrincipalUserDetails principal = (PrincipalUserDetails) authentication.getPrincipal();
        return principal.getUserEntity().getId().toString();
    }

    private void writeBodyWithUserDto(HttpServletResponse response, Authentication authentication) throws IOException {
        UserDto user = UserDto.from(authentication);
        CommonResponse<UserDto> common = CommonResponse.<UserDto>builder()
                .status(HttpServletResponse.SC_OK)
                .message("OK")
                .data(user)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(common));
    }
}
