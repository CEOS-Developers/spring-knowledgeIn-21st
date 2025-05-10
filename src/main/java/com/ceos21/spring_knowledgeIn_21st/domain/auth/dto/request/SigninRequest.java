package com.ceos21.spring_knowledgeIn_21st.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청")
public class SigninRequest {
    @Schema(description = "이메일", example = "cherry@fruits.com")
    private String email;

    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
