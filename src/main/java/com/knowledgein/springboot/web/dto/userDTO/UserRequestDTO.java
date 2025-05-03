package com.knowledgein.springboot.web.dto.userDTO;

import com.knowledgein.springboot.domain.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpRequestDto {
        @Schema(description = "닉네임", example = "tris")
        @NotNull
        private String nickName;

        @Schema(description = "이메일", example = "tris@gmail.com")
        @NotNull
        private String email;

        @Schema(description = "비밀번호", example = "tris123")
        @NotNull
        private String password;

        @Schema(description = "일반유저 / 어드민", example = "USER")
        private RoleType roleType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignInRequestDto {
        @Schema(description = "이메일", example = "tris@gmail.com")
        @NotNull
        private String email;

        @Schema(description = "비밀번호", example = "tris123")
        @NotNull
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReissueRequestDto {
        @Schema(description = "리프레시 토큰", example = "dklksdfsdklkfds8326v5cf5d5d6s6flk9876542316468645")
        @NotNull
        private String refreshToken;
    }
}
