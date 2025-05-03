package com.knowledgein.springboot.web.dto.userDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponseDto {
        @Schema(description = "액세스 토큰", example = "adfijgk45467dd5824vsfddfgklefk12345678912345648")
        private String accessToken;

        @Schema(description = "리프레시 토큰", example = "dklksdfsdklkfds8326v5cf5d5d6s6flk9876542316468645")
        private String refreshToken;

        @Schema(description = "닉네임", example = "tris")
        private String nickName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshTokenResponseDto {
        @Schema(description = "액세스 토큰", example = "adfijgk45467dd5824vsfddfgklefk12345678912345648")
        private String accessToken;

        @Schema(description = "리프레시 토큰", example = "dklksdfsdklkfds8326v5cf5d5d6s6flk9876542316468645")
        private String refreshToken;
    }
}
