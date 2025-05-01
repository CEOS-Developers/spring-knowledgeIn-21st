package com.ceos21.ceos21BE.web.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private Long userId;
    private String username;
    private String email;
}
