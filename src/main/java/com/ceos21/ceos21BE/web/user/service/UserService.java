package com.ceos21.ceos21BE.web.user.service;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;
import com.ceos21.ceos21BE.web.user.dto.UserInfoDto;
import com.ceos21.ceos21BE.web.user.entity.User;
import com.ceos21.ceos21BE.web.user.handler.UserHandler;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserInfoDto getUserInto(Long userId) {
        User user = validateUser(userId);

        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


    @Transactional
    public void deleteUserAccount(Long userId) {
        User user = validateUser(userId);

        // soft delete 하고 싶으면 isDeleted = true 로 바꿔줌
        userRepository.delete(user);
    }

    public boolean checkEmailDuplicate(String email) {
        boolean exists =  userRepository.existsByEmail(email);
        return !exists;
    }

    @Transactional
    public void updateUsername(Long userId, String newUsername) {
        User user = validateUser(userId);
        user.updateUsername(newUsername);
    }

    public User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus._USER_NOT_FOUND));
    }
}
