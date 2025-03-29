package com.ceos21.spring_boot.domain.users.dto;

import com.ceos21.spring_boot.domain.users.Users;
import lombok.Getter;

@Getter
public class UsersRequest {

    private String name;
    private String email;
    private String provider;
    private String hashPassword;
    private String salt;

    public Users toEntity() {
        return Users.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .hashPassword(hashPassword)
                .salt(salt)
                .build();
    }
}
