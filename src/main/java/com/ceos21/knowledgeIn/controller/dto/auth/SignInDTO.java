package com.ceos21.knowledgeIn.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder @AllArgsConstructor
@Getter
@NoArgsConstructor
public class SignInDTO {
    private String username;
    private String password;
}
