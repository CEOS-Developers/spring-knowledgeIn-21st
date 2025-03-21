package com.ceos21.ceos21BE.apiPayload.code.status;

import com.ceos21.ceos21BE.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000", "서버 에러, 관리자에게 문의 바랍니다."),
    _MAILSENDER_ERROR(HttpStatus.BAD_REQUEST,"COMMON5001","인증 이메일 전송에 실패하였습니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON4000","잘못된 요청입니다."),
    _TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "Exception test 입니다."),
    _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "COMMON4002", "중복된 이메일입니다."),
    _BAD_PASSWORD(HttpStatus.BAD_REQUEST, "COMMON4003", "잘못된 패스워드입니다."),
    _NON_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "COMMON4004", "존재하지 않는 이메일입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON4010","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4030", "금지된 요청입니다."),
    _FORBIDDEN_PASSWORD(HttpStatus.FORBIDDEN, "COMMON4031","불가능한 패스워드입니다. 패스워드는 영어,숫자 8-13글자만 가능합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
