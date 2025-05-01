package com.ceos21.ceos21BE.web.auth.converter;

import com.ceos21.ceos21BE.web.user.entity.UserEntity;

public class AuthConverter {
    public static UserEntity toUser(String email, String name, String password) {

        // 나중에 social login 시에 사용
        String passwordToUse = password != null ? password : "defaultPassword";

        return UserEntity.builder()
                .email(email)
                .username(name)
                .password(passwordToUse)
                .role("ROLE_USER")
                .build();

    }


}
