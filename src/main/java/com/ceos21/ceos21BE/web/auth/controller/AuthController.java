package com.ceos21.ceos21BE.web.auth.controller;

import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.global.apiPayload.code.status.SuccessStatus;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;
import com.ceos21.ceos21BE.jwt.JwtUtil;
import com.ceos21.ceos21BE.jwt.dto.JwtDto;
import com.ceos21.ceos21BE.web.auth.dto.LoginRequestDTO;
import com.ceos21.ceos21BE.web.auth.dto.SignUpRequestDTO;
import com.ceos21.ceos21BE.web.auth.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<JwtDto> login(@RequestBody LoginRequestDTO request) {
        JwtDto jwtDto = authService.login(request.getEmail(), request.getPassword());
        return ApiResponse.onSuccess(jwtDto);
    }

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@RequestBody SignUpRequestDTO request) {
        authService.signUp(request);
        return ApiResponse.of(SuccessStatus._CREATED, null);
    }

    @GetMapping("/reissue")
    public ApiResponse<JwtDto> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            jwtUtil.validateRefreshToken(refreshToken);
            return ApiResponse.onSuccess(jwtUtil.reissueToken(refreshToken));

        } catch (SignatureException e) {
            throw new GeneralException("Invalid refresh token");
        } catch (ExpiredJwtException e) {
            throw new GeneralException("Refresh token expired");
        }
    }
}
