package com.ceos21.knowledgein.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RequestJoin(
        @NotBlank
        String email,
        @NotBlank
        String name,
        @NotBlank
        String nickName,
        @NotBlank
        String password
) {
}
