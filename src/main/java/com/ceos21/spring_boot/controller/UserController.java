package com.ceos21.spring_boot.controller;

import com.ceos21.spring_boot.base.ApiResponse;
import com.ceos21.spring_boot.base.auth.JwtTokenProvider;
import com.ceos21.spring_boot.base.status.SuccessStatus;
import com.ceos21.spring_boot.domain.entity.User;
import com.ceos21.spring_boot.dto.user.LoginRequestDTO;
import com.ceos21.spring_boot.dto.user.LoginResponseDTO;
import com.ceos21.spring_boot.dto.user.UserResponseDTO;
import com.ceos21.spring_boot.dto.user.UserSignupRequestDTO;
import com.ceos21.spring_boot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Operation(summary="회원가입")
    @PostMapping("/create")
    public ApiResponse<UserResponseDTO> signUp(@RequestBody @Valid UserSignupRequestDTO request) {

        // 성공 응답 + 회원가입 결과 DTO 반환
        UserResponseDTO result = userService.signUp(request);
        return ApiResponse.of(SuccessStatus._OK, result);
    }

    // 로그인
    @Operation(summary= "로그인")
    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){

        // 사용자 이메일로 사용자 조회
        LoginResponseDTO result = userService.login(request);

        // 성공 응답 반환
        return ApiResponse.of(SuccessStatus._OK, result);
    }


}
