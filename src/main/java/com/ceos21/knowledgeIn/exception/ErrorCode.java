package com.ceos21.knowledgeIn.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    THIS_IS_EXAMPLE(HttpStatus.BAD_REQUEST, "예시용 에러 코드입니다"),
    CAN_NOT_SAVE_IMAGE(HttpStatus.BAD_REQUEST, "이미지 파일 저장에 문제가 발생했습니다"),
    CAN_NOT_DELETE_IMAGE(HttpStatus.BAD_REQUEST, "이미지 파일 삭제에 문제가 발생했습니다"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "중복된 아이디가 존재합니다"),
    SIGNIN_FAILED(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰 검증에 실패했습니다."),
    TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "연관된 토큰이 아닙니다"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효한 토큰이 아닙니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
