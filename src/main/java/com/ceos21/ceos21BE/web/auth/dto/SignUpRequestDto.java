package com.ceos21.ceos21BE.web.auth.dto;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String username;
    private String email;
    private String password;
}
