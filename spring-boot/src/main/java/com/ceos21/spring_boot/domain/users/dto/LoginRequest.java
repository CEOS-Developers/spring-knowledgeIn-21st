package com.ceos21.spring_boot.domain.users.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
