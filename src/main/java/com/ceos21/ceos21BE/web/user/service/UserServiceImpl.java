package com.ceos21.ceos21BE.web.user.service;

import com.ceos21.ceos21BE.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.apiPayload.exception.GeneralException;
import com.ceos21.ceos21BE.web.user.dto.UserInfoDto;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Override
    public UserInfoDto getUserInto(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public void deleteUserAccount(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        // soft delete 하고 싶으면 isDeleted = true 로 바꿔줌
        userRepository.delete(user);
    }
}
