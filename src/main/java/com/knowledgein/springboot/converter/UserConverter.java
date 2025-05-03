package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.jwt.TokenProvider;
import com.knowledgein.springboot.web.dto.userDTO.UserRequestDTO;
import com.knowledgein.springboot.web.dto.userDTO.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserConverter {
    public static User toAuthUser(UserRequestDTO.SignUpRequestDto request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleType(request.getRoleType())
                .build();
    }

    public static UserResponseDTO.LoginResponseDto toLoginResponseDto(String accessToken, String refreshToken, String nickName) {
        return UserResponseDTO.LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickName(nickName)
                .build();
    }

    public static UserResponseDTO.RefreshTokenResponseDto toRefreshTokenResponseDto(TokenProvider.TokenPair tokenPair) {
        return UserResponseDTO.RefreshTokenResponseDto.builder()
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .build();
    }
}
