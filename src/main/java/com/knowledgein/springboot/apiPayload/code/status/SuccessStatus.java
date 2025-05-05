package com.knowledgein.springboot.apiPayload.code.status;

import com.knowledgein.springboot.apiPayload.code.BaseCode;
import com.knowledgein.springboot.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 기본 성공 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 로그인 관련 성공 응답
    COMPLETE_SIGNUP(HttpStatus.CREATED, "COMPLETE_SIGNUP", "회원가입이 완료되었습니다."),
    COMPLETE_LOGIN(HttpStatus.OK, "COMPLETE_LOGIN", "로그인이 완료되었습니다."),
    COMPLETE_LOGOUT(HttpStatus.OK, "COMPLETE_LOGOUT", "로그아웃이 완료되었습니다."),
    REISSUED_ACCESS_TOKEN(HttpStatus.OK, "REISSUED_ACCESS_TOKEN", "엑세스 토큰이 재발급되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
