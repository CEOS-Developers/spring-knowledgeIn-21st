package com.ceos21.ceos21BE.web.user.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginResponseDto {

    private Long userId;
    private String userName;
    private String token;
}
