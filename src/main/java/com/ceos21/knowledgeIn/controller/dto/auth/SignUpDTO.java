package com.ceos21.knowledgeIn.controller.dto.auth;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class SignUpDTO {
    private String name;

    private String nickname;

    private String username;

    private String password;

}
