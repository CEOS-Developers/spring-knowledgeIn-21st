package com.ceos21.knowledgein.security.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RequestLogin(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
