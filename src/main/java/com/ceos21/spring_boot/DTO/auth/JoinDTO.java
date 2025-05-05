package com.ceos21.spring_boot.DTO.auth;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class JoinDTO {
    private String userId;
    private String username;
    private String password;
    private String email;
    private LocalDate birthdate;
    private String phoneNumber;
}
