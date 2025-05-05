package com.ceos21.knowledgein.security.exception;

import com.ceos21.knowledgein.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SecurityImplementErrorCode implements ErrorCode {

    ILLEGAL_REGISTRATION_ID(HttpStatus.BAD_REQUEST, "허용되지 않는 소셜로그인입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
