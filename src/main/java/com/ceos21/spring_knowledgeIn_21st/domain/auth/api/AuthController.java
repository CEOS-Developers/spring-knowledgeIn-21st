package com.ceos21.spring_knowledgeIn_21st.domain.auth.api;

import com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.response.RefreshResponse;
import com.ceos21.spring_knowledgeIn_21st.domain.user.application.UserService;
import com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.request.SigninRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.request.SignupRequest;
import com.ceos21.spring_knowledgeIn_21st.global.common.ApiResponse;
import com.ceos21.spring_knowledgeIn_21st.global.jwt.JwtUtil;
import com.ceos21.spring_knowledgeIn_21st.global.jwt.RefreshTokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequest request) {
        userService.register(request);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인합니다. 실제 처리는 JwtAuthenticationFilter에서 수행"
    )
    @PostMapping("/signin")
    public void signin(@RequestBody SigninRequest request) {
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshResponse>> refresh(
            @RequestHeader("Refresh-Token") String refreshToken) {
        String accessToken = userService.reissue(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(new RefreshResponse(accessToken)));
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);   // "Authorization" 헤더에서 access token 추출
        userService.logout(token);

        return  ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null));
    }
}
