package com.ceos21.ceos21BE.web.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String username;
    private String email;

    public static UserInfoDto from(UserInfoDto userInfoDto) {
        return UserInfoDto.builder()
                .username(userInfoDto.getUsername())
                .email(userInfoDto.getEmail())
                .build();
    }
}
