package com.knowledgein.springboot.web.controller;

import com.knowledgein.springboot.apiPayload.ApiResponse;
import com.knowledgein.springboot.apiPayload.code.status.SuccessStatus;
import com.knowledgein.springboot.jwt.CustomUserDetails;
import com.knowledgein.springboot.jwt.JwtAuthenticationFilter;
import com.knowledgein.springboot.service.authService.AuthCommandService;
import com.knowledgein.springboot.web.dto.userDTO.UserRequestDTO;
import com.knowledgein.springboot.web.dto.userDTO.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="로그인", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
public class UserRestController {
    private final AuthCommandService authService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/sign-up")
    @Operation(summary = "일반 회원가입 API")
    public ApiResponse<?> signUp(@Valid @RequestBody UserRequestDTO.SignUpRequestDto signUpRequest) {
        authService.handleUserRegistration(signUpRequest);
        return ApiResponse.onSuccess(SuccessStatus.COMPLETE_SIGNUP);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "일반 로그인 API")
    public ApiResponse<UserResponseDTO.LoginResponseDto> signIn(@Valid @RequestBody UserRequestDTO.SignInRequestDto signInRequest) {
        return ApiResponse.onSuccess(authService.handleUserLogin(signInRequest));
    }

    @PostMapping("/sign-out")
    @Operation(summary = "일반 로그아웃 API")
    public ApiResponse<?> signOut(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authService.handleUserLogout(jwtAuthenticationFilter.resolveToken(request), customUserDetails.getUser());
        return ApiResponse.onSuccess(SuccessStatus.COMPLETE_LOGOUT);
    }

    @PostMapping("/reissue")
    @Operation(summary = "리프레시 토큰으로 액세스 토큰 재발급 API")
    public ApiResponse<UserResponseDTO.RefreshTokenResponseDto> reissueAccessToken(@RequestBody UserRequestDTO.ReissueRequestDto reissueRequest,
                                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.onSuccess(authService.reissueAccessToken(reissueRequest.getRefreshToken(), customUserDetails));
    }
}
