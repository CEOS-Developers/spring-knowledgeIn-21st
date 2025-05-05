package com.ceos21.springknowledgein.domain.user.controller;

import com.ceos21.springknowledgein.domain.user.dto.SignupRequestDto;
import com.ceos21.springknowledgein.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto dto) {
        memberService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }


}
