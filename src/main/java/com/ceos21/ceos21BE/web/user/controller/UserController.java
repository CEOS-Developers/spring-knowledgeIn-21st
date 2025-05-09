package com.ceos21.ceos21BE.web.user.controller;

import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.web.user.customDetail.CustomDetails;
import com.ceos21.ceos21BE.web.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Authorization: Bearer <access_token> 형식으로 보내야 함
    @GetMapping("/info")
    public ApiResponse<UserInfoDto> getUserInfo(@AuthenticationPrincipal CustomDetails customDetails) {
        // 아래는 getUsername이라고 override해서 썼는데 나는 이름 말고 email로 찾고 가져옴.
        UserInfoDto userInfoDto = userService.getUserInto(customDetails.getUsername());
        return ApiResponse.onSuccess(userInfoDto);
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUserAccount(@AuthenticationPrincipal CustomDetails customDetails) {
        userService.deleteUserAccount(customDetails.getUsername());
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }
}
