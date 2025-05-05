package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.auth.jwt.JwtTokenProvider;
import com.ceos21.knowledgeIn.auth.jwt.blacklist.BlacklistTokenService;
import com.ceos21.knowledgeIn.auth.jwt.refresh.RefreshTokenService;
import com.ceos21.knowledgeIn.controller.dto.auth.ReissueResponseDTO;
import com.ceos21.knowledgeIn.controller.dto.auth.SignInDTO;
import com.ceos21.knowledgeIn.controller.dto.auth.SignInResponseDTO;
import com.ceos21.knowledgeIn.controller.dto.auth.SignUpDTO;
import com.ceos21.knowledgeIn.controller.dto.user.UserResponseDTO;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.exception.CustomJisikInException;
import com.ceos21.knowledgeIn.exception.ErrorCode;
import com.ceos21.knowledgeIn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final BlacklistTokenService blacklistTokenService;

    //create(signup)
    @Transactional
    public Long signUp(SignUpDTO dto) {
        // id  중복 체크
        String username = dto.getUsername();
        if (!userRepository.findByUsername(username).isEmpty()) {
            throw new CustomJisikInException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User entity = User.builder()
                .name(dto.getName())
                .nickname(dto.getNickname())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        return userRepository.save(entity).getId();
    }

    public String signIn(SignInDTO dto) {
        User findUser = userRepository.findByUsername(dto.getUsername()).orElseThrow();

        // 비번 검증
        if (!passwordEncoder.matches(dto.getPassword(), findUser.getPassword())) {
            throw new CustomJisikInException(ErrorCode.SIGNIN_FAILED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(findUser.getId());


        return accessToken;
    }

    /*
   accessToken: 헤더에서 취함
   refreshToken: body에서 취함
   */
    public ReissueResponseDTO reissue(String accessHeader, String refreshToken) {
        String accessToken = accessHeader.replace("Bearer ", "");

        // refresh token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomJisikInException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 본인 refresh token인지 검증
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        String savedRefreshToken = refreshTokenService.getToken(userId);
        if (!refreshToken.equals(savedRefreshToken)) {
            throw new CustomJisikInException(ErrorCode.TOKEN_MISMATCH);
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

        return new ReissueResponseDTO(newAccessToken);
    }

    // 로그아웃: 블랙리스트 등록 & refreshToken삭제
    public void logout(String accessHeader) {
        String accessToken = accessHeader.replace("Bearer ", "");

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomJisikInException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        // 1. refreshToken 삭제
        refreshTokenService.deleteToken(userId);

        // 2. accessToken을 블랙리스트로 등록
        long remainExpiration = jwtTokenProvider.getRemainExpiration(accessToken);
        blacklistTokenService.blacklistAccessToken(accessToken, remainExpiration);
    }

    //read
    public UserResponseDTO findUser(Long id) {
        User findUser = userRepository.findById(id).orElseThrow(()->new RuntimeException("cannot find user by id"));

        return UserResponseDTO.from(findUser);
    }

    public List<UserResponseDTO> findAllUsers() {
        List<UserResponseDTO> dtos = userRepository.findAll()
                .stream()
                .map(user->UserResponseDTO.from(user))
                .collect(Collectors.toList());

        return dtos;
    }

    //update

    //delete
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
