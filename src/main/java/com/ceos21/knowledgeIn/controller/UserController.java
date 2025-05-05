package com.ceos21.knowledgeIn.controller;

import com.ceos21.knowledgeIn.auth.jwt.JwtTokenProvider;
import com.ceos21.knowledgeIn.auth.jwt.refresh.RefreshTokenService;
import com.ceos21.knowledgeIn.controller.dto.auth.*;
import com.ceos21.knowledgeIn.controller.dto.user.UserResponseDTO;
import com.ceos21.knowledgeIn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/users/signup")
    public ResponseEntity<Long> signUp(@RequestBody SignUpDTO dto) {
        Long id = userService.signUp(dto);

        return ResponseEntity.ok(id);
    }

    // 로그인
    @PostMapping("/users/signin")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInDTO dto) {
        String accessToken = userService.signIn(dto);
        String refreshToken = refreshTokenService.createToken(tokenProvider.getUserIdFromToken(accessToken), 60 * 60 * 24 * 7);


        return ResponseEntity.ok(new SignInResponseDTO(accessToken, refreshToken));
    }

    // 액세스 토큰 재발행
    @PostMapping("/users/reissue")
    public ResponseEntity<ReissueResponseDTO> reissue(@RequestHeader("Authorization") String accessHeader, @RequestBody ReissueDTO dto) {
        ReissueResponseDTO responseDTO = userService.reissue(accessHeader, dto.getRefreshToken());

        return ResponseEntity.ok(responseDTO);
    }

    // 로그아웃
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessHeader) {
        userService.logout(accessHeader);

        return ResponseEntity.ok().build();
    }

    // read
    @GetMapping("/users/{userId}")
    public UserResponseDTO findUser(@PathVariable Long userId) {

        return userService.findUser(userId);
    }

    //회원정보 수정 => 실제 회원가입 전략에 따라 구현
//    @PutMapping("/users")

    //회원탈퇴 => 실제 회원가입 전략에 따라 구현
/*
    @DeleteMapping("/users")
    public void deleteUser() {

    }
*/
}
