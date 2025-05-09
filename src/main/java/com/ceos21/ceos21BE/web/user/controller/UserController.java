package com.ceos21.ceos21BE.web.user.controller;

import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.global.apiPayload.code.status.SuccessStatus;
import com.ceos21.ceos21BE.web.user.customDetail.CustomDetails;
import com.ceos21.ceos21BE.web.user.dto.UserInfoDto;
import com.ceos21.ceos21BE.web.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    // 내 정보 조회
    @GetMapping("/info")
    public ApiResponse<UserInfoDto> getUserInfo(@AuthenticationPrincipal CustomDetails customDetails) {
        // 아래는 getUsername이라고 override해서 썼는데 나는 이름 말고 email로 찾고 가져옴.
        UserInfoDto userInfoDto = userService.getUserInto(customDetails.getUser().getUserId());
        return ApiResponse.onSuccess(userInfoDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUserAccount(@AuthenticationPrincipal CustomDetails customDetails) {
        userService.deleteUserAccount(customDetails.getUser().getUserId());
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }

    //닉네임 수정
    @PatchMapping("/me")
    public ApiResponse<Void> updateUsername(
            @RequestBody @Valid String newUsername,
            @AuthenticationPrincipal CustomDetails customDetails) {
        userService.updateUsername(customDetails.getUser().getUserId(), newUsername);
        return ApiResponse.of(SuccessStatus._UPDATED, null);
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean isAvailable = userService.checkEmailDuplicate(email);
        return ApiResponse.onSuccess(isAvailable);
    }

}
