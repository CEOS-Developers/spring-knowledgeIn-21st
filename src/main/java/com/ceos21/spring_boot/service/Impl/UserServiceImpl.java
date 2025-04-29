package com.ceos21.spring_boot.service.Impl;

import com.ceos21.spring_boot.base.auth.JwtTokenProvider;
import com.ceos21.spring_boot.base.exception.CustomException;
import com.ceos21.spring_boot.converter.PostConverter;
import com.ceos21.spring_boot.converter.UserConverter;
import com.ceos21.spring_boot.domain.entity.User;
import com.ceos21.spring_boot.base.status.ErrorStatus;
import com.ceos21.spring_boot.dto.user.LoginRequestDTO;
import com.ceos21.spring_boot.dto.user.LoginResponseDTO;
import com.ceos21.spring_boot.dto.user.UserResponseDTO;
import com.ceos21.spring_boot.repository.UserRepository;
import com.ceos21.spring_boot.service.UserService;
import com.ceos21.spring_boot.dto.user.UserSignupRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public UserResponseDTO signUp(UserSignupRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorStatus.DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 객체 생성
        User user = UserConverter.toUser(request, encodedPassword);


        // User 저장
        userRepository.save(user);

        return UserConverter.toUserResponseDTO(user);

    }

    //로그인
    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorStatus.INVALID_PASSWORD);
        }

        // JWT 생성
        String accessToken = jwtTokenProvider.createToken(user);

        return UserConverter.toLoginResponseDTO(user, accessToken);
    }

    // 로그아웃
    public void logout(String token) {
        jwtTokenProvider.invalidateToken(token);

    }
}