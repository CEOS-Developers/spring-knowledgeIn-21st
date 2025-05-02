package com.ceos21.knowledgein.user.service;

import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.dto.UserDto;
import com.ceos21.knowledgein.user.dto.request.RequestJoin;
import com.ceos21.knowledgein.user.exception.AuthException;
import com.ceos21.knowledgein.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.ceos21.knowledgein.user.exception.AuthErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDto join(RequestJoin requestJoin) {
        UserEntity user = UserEntity.of(
                requestJoin.email(),
                requestJoin.name(),
                requestJoin.nickName(),
                passwordEncoder.encode(requestJoin.password())
        );

        userRepository.save(user);

        return UserDto.from(user);
    }

    public UserEntity findUserByIdReturnEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(USER_NOT_FOUND));
    }

}
