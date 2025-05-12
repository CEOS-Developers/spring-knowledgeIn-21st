package com.ceos21.ceos21BE.web.auth.dto;

import lombok.Getter;

@Getter
public class SignUpRequestDTO {
    private String username;
    private String email;
    private String password;
}
