package com.ceos21.knowledgeIn.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SignInResponseDTO {
    private String accessToken;

    private String refreshToken;
}
