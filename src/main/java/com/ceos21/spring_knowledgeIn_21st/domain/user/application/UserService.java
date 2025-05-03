package com.ceos21.spring_knowledgeIn_21st.domain.user.application;

import com.ceos21.spring_knowledgeIn_21st.domain.user.dao.UserRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.request.SignupRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.user.enums.UserRole;
import com.ceos21.spring_knowledgeIn_21st.global.exception.CustomException;
import com.ceos21.spring_knowledgeIn_21st.global.exception.ErrorCode;
import com.ceos21.spring_knowledgeIn_21st.global.jwt.JwtUtil;
import com.ceos21.spring_knowledgeIn_21st.global.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos21.spring_knowledgeIn_21st.global.exception.ErrorCode.DUPLICATE_EMAIL;
import static com.ceos21.spring_knowledgeIn_21st.global.exception.ErrorCode.INVALID_REFRESH_TOKEN;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void register(SignupRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        userRepository.save(request.toEntity(passwordEncoder));
    }

    @Transactional
    public String reissue(String refreshToken) {
        jwtUtil.validateToken(refreshToken);

        String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
        String saved = refreshTokenRepository.get(email);

        if (saved == null) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (!saved.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtUtil.createAccessToken(email, UserRole.USER.name());
    }

    @Transactional
    public void logout(String refreshToken) {
        jwtUtil.validateToken(refreshToken);   // 만료되었으면 예외 던짐

        String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
        refreshTokenRepository.delete(email);   // 리프레시 토큰 삭제

    }
}