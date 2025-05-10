package com.ceos21.ceos21BE.web.auth.converter;

import com.ceos21.ceos21BE.web.user.entity.User;

public class AuthConverter {
    public static User toUser(String email, String name, String password) {

        // 나중에 social login 시에 사용
        String passwordToUse = password != null ? password : "defaultPassword";

        return User.builder()
                .email(email)
                .username(name)
                .password(passwordToUse)
                .role("ROLE_USER")
                .build();

    }


}
