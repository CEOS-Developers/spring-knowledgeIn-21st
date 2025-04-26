package com.ceos21.spring_boot.service;

import com.ceos21.spring_boot.dto.user.LoginRequestDTO;
import com.ceos21.spring_boot.dto.user.LoginResponseDTO;
import com.ceos21.spring_boot.dto.user.UserResponseDTO;
import com.ceos21.spring_boot.dto.user.UserSignupRequestDTO;

public interface UserService {
    UserResponseDTO signUp(UserSignupRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
}
