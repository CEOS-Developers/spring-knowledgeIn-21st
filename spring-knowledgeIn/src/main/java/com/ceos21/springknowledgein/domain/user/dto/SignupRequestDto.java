package com.ceos21.springknowledgein.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String role; //요청 들어올 떄는 json으로 들어오기에 일단 String으로 받고 서비스에서 UserRoleEnum으로 변경

    public SignupRequestDto(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
