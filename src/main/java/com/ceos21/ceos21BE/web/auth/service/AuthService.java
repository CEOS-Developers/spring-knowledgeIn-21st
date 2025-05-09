package com.ceos21.ceos21BE.web.auth.service;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;
import com.ceos21.ceos21BE.web.user.customDetail.CustomDetails;
import com.ceos21.ceos21BE.jwt.JwtUtil;
import com.ceos21.ceos21BE.jwt.dto.JwtDto;
import com.ceos21.ceos21BE.web.auth.dto.SignUpRequestDto;
import com.ceos21.ceos21BE.web.user.entity.User;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public JwtDto login(String email, String password) {
        // 1. 이메일로 유저 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new GeneralException(ErrorStatus._PASSWORD_NOT_MATCH);
        }

        CustomDetails customDetails = new CustomDetails(user);

        // Jwt
        String accessToken = jwtUtil.createJwtAccessToken(customDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(customDetails);

        return new JwtDto(accessToken, refreshToken);
    }

    public JwtDto signUp(SignUpRequestDto requestDto) {
        // email 중복 체크
        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new GeneralException(ErrorStatus._EMAIL_DUPLICATED);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // UserEntity 생성
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .username(requestDto.getUsername())
                .role("ROLE_USER")
                .build();

        // UserEntity 저장
        userRepository.save(user);

        // Jwt
        CustomDetails customDetails = new CustomDetails(user);
        String accessToken = jwtUtil.createJwtAccessToken(customDetails);
        String refreshToken = jwtUtil.createJwtRefreshToken(customDetails);

        return new JwtDto(accessToken, refreshToken);
    }
}
