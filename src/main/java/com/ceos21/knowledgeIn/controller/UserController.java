package com.ceos21.knowledgeIn.controller;

import com.ceos21.knowledgeIn.controller.dto.user.UserResponseDTO;
import com.ceos21.knowledgeIn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {
    private final UserService userService;

    //회원가입 => 실제 회원가입 전략에 따라 구현
//    @PostMapping("/users")

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
