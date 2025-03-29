package com.knowledgein.springboot.apiPayload.code.status;

import com.knowledgein.springboot.apiPayload.code.BaseCode;
import com.knowledgein.springboot.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // 기본 에러 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 유저 관련 에러 응답
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND", "유저를 찾을 수 없습니다."),

    // 게시물 관련 에러 응답
    PARENT_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PARENT QUESTION NOT FOUND", "부모 질문글을 찾을 수 없습니다."),
    QUESTION_SHOULD_NOT_EXIST(HttpStatus.BAD_REQUEST, "QUESTION SHOULD NOT EXIST", "질문글은 부모 질문글을 가질 수 없습니다."),
    QUESTION_SHOULD_EXIST(HttpStatus.BAD_REQUEST, "QUESTION SHOULD EXIST", "대답글은 부모 질문글을 가져야 합니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST NOT FOUND", "게시물을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}