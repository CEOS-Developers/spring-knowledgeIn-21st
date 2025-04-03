package com.ceos21.knowledgein.user.service;

import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.exception.AuthException;
import com.ceos21.knowledgein.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ceos21.knowledgein.user.exception.AuthErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findUserByIdReturnEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(USER_NOT_FOUND));
    }

}
