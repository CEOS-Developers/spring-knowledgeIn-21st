package com.ceos21.ceos21BE.global.apiPayload.code.status;

import com.ceos21.ceos21BE.global.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.global.apiPayload.code.ErrorReasonDTO;
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
    _FORBIDDEN_PASSWORD(HttpStatus.FORBIDDEN, "COMMON4031","불가능한 패스워드입니다. 패스워드는 영어,숫자 8-13글자만 가능합니다."),
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4000", "해당 사용자를 찾을 수 없습니다."),
    _QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4001", "해당 질문을 찾을 수 없습니다."),
    _POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4000", "해당 글을 찾을 수 없습니다."),
    _ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4002", "해당 답변글을 찾을 수 없습니다."),
    _HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "HASHTAG4000", "해당 해시태그가 존재하지 않습니다."),
    _QUESTION_CREATE_FAIL(HttpStatus.BAD_REQUEST, "POST4003", "질문 생성에 실패하였습니다."),
    _QUESTION_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "POST4004", "질문 수정에 실패하였습니다."),
    _QUESTION_CANNOT_UPDATE_WITH_ANSWER(HttpStatus.BAD_REQUEST, "POST4005", "질문은 답변이 달리면 수정할 수 없습니다."),
    _REACTION_NOT_VALID(HttpStatus.BAD_REQUEST, "POST4006", "잘못된 리액션입니다."),
    _QUESTION_DELETE_FAIL(HttpStatus.BAD_REQUEST, "QUESTION4003", "질문 삭제에 실패하였습니다."),
    _HASHTAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "HASHTAG4001", "이미 존재하는 해시태그입니다."),
    _COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4000", "해당 댓글을 찾을 수 없습니다."),
    _USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "COMMENT4001", "해당 댓글에 대한 권한이 없습니다."),
    _EMAIl_NOT_FOUNd(HttpStatus.NOT_FOUND, "COMMENT4002", "해당 이메일을 찾을 수 없습니다."),
    _EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "COMMENT4002", "중복된 이메일입니다."),
    _PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "COMMENT4003", "비밀번호가 일치하지 않습니다."),
    _TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4004", "해당 토큰을 찾을 수 없습니다."),
    _TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "COMMENT4005", "해당 토큰이 만료되었습니다."),
    _TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "COMMENT4006", "해당 토큰이 유효하지 않습니다."),;


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
