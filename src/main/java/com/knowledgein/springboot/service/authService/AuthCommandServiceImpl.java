package com.knowledgein.springboot.service.authService;

import com.knowledgein.springboot.apiPayload.code.status.ErrorStatus;
import com.knowledgein.springboot.apiPayload.exception.GeneralException;
import com.knowledgein.springboot.converter.UserConverter;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.jwt.CustomUserDetails;
import com.knowledgein.springboot.jwt.TokenProvider;
import com.knowledgein.springboot.redis.RedisService;
import com.knowledgein.springboot.repository.UserRepository;
import com.knowledgein.springboot.web.dto.userDTO.UserRequestDTO;
import com.knowledgein.springboot.web.dto.userDTO.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    @Transactional
    public void handleUserRegistration(UserRequestDTO.SignUpRequestDto request) {
        User existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (existingUser != null) throw new GeneralException(ErrorStatus.USER_ALREADY_EXISTS);
        if (userRepository.findByNickName(request.getNickName()).isPresent()) throw new GeneralException(ErrorStatus.NICKNAME_ALREADY_EXISTS);

        User newUser = UserConverter.toAuthUser(request, passwordEncoder);
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public UserResponseDTO.LoginResponseDto handleUserLogin(UserRequestDTO.SignInRequestDto request) {
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCH);

        String accessToken = tokenProvider.createAccessToken(existingUser.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(existingUser.getEmail());

        // 로그인 시 refreshToken을 redis에 저장
        redisService.setValue(existingUser.getEmail(), refreshToken, 1000 * 60 * 60 * 24 * 7L);

        return UserConverter.toLoginResponseDto(accessToken, refreshToken, existingUser.getNickName());
    }

    @Override
    @Transactional(readOnly = true)
    public void handleUserLogout(String accessToken, User existingUser) {
        if (existingUser == null) throw new GeneralException(ErrorStatus.USER_NOT_FOUND);

        // 로그아웃 시 refreshToken을 redis에서 삭제하고 accessToken을 redis에 저장
        redisService.deleteValue(existingUser.getEmail()); // 재로그인 방지
        redisService.setValue(accessToken, "logout", tokenProvider.getExpirationTime(accessToken)); // AccessToken을 블랙리스트에 저장
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO.RefreshTokenResponseDto reissueAccessToken(String refreshToken, CustomUserDetails customUserDetails) {
        if (customUserDetails.getUser() == null) throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        User existingUser = customUserDetails.getUser();

        // validateToken false 반환 -> 사용하려는 refreshToken이 유효하지 않음 (redis에 없음)
        if (!tokenProvider.validateToken(refreshToken)) throw new GeneralException(ErrorStatus.INVALID_TOKEN);

        String redisRefreshToken = redisService.getValue(existingUser.getEmail());
        if (StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(redisRefreshToken) || !redisRefreshToken.equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }

        TokenProvider.TokenPair tokenPair = tokenProvider.reissue(existingUser, refreshToken);
        return UserConverter.toRefreshTokenResponseDto(tokenPair);
    }
}
