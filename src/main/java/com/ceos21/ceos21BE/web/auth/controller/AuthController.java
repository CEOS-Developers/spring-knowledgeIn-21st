package com.ceos21.ceos21BE.web.auth.controller;

import com.ceos21.ceos21BE.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.jwt.JwtUtil;
import com.ceos21.ceos21BE.jwt.dto.JwtDto;
import com.ceos21.ceos21BE.web.auth.dto.LoginRequestDto;
import com.ceos21.ceos21BE.web.auth.dto.SignUpRequestDto;
import com.ceos21.ceos21BE.web.auth.service.AuthService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtDto> login(@RequestBody LoginRequestDto request) {
        JwtDto jwtDto = authService.login(request.getEmail(), request.getPassword());
        return ApiResponse.onSuccess(jwtDto);
    }

    @PostMapping("/signup")
    public ApiResponse<JwtDto> signUp(@RequestBody SignUpRequestDto request) {
        JwtDto jwtDto = authService.signUp(request);
        return ApiResponse.onSuccess(jwtDto);
    }

}
