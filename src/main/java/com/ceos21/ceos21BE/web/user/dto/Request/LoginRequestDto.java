package com.ceos21.ceos21BE.web.user.dto.Request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {

    private String email;
    private String password;

}
