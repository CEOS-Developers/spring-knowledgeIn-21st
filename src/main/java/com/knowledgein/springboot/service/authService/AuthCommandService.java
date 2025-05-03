package com.knowledgein.springboot.service.authService;

import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.jwt.CustomUserDetails;
import com.knowledgein.springboot.web.dto.userDTO.UserRequestDTO;
import com.knowledgein.springboot.web.dto.userDTO.UserResponseDTO;

public interface AuthCommandService {
    void handleUserRegistration(UserRequestDTO.SignUpRequestDto request);
    UserResponseDTO.LoginResponseDto handleUserLogin(UserRequestDTO.SignInRequestDto request);
    void handleUserLogout(String token, User existingUser);
    UserResponseDTO.RefreshTokenResponseDto reissueAccessToken(String refreshToken, CustomUserDetails customUserDetails);
}
