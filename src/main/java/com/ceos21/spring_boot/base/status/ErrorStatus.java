package com.ceos21.spring_boot.base.status;

import com.ceos21.spring_boot.base.BaseErrorCode;
import com.ceos21.spring_boot.base.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 0. 공통 에러
    COMMON_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러가 발생했습니다. 관리자에게 문의하세요."),
    COMMON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_4001", "잘못된 요청입니다."),
    COMMON_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_4002", "인증이 필요합니다."),
    COMMON_WRONG_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON_4003", "잘못된 파라미터 값 입니다."),

    //1. Post 관련 에러
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_4001", "해당 질문을 찾을 수 없습니다."),
    CANNOT_DELETE(HttpStatus.BAD_REQUEST, "POST_4002", "해당 질문을 삭제할 권한이 없습니다."),

    //2. User 관련 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_4001", "해당 유저를 찾을 수 없습니다."),

    //3. 이미지 관련 에러
    IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "IMAGE_4001", "이미지 업로드 실패"),

    //4. Answer 관련 에러
    CANNOT_ANSWER(HttpStatus.BAD_REQUEST, "Answer_4001", "본인의 글엔 답변 불가합니다."),
    ANSWER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Answer_4002", "해당 답변을 찾을 수 없습니다."),

    //5. 좋아요 싫어요 관련 에러
    CANNOT_CHECK_BOTH(HttpStatus.BAD_REQUEST, "LikeDislike_4001", "좋아요와 싫어요를 모두 누를 수 없습니다."),
    DUPLICATE_LIKE(HttpStatus.BAD_REQUEST, "LikeDislike_4002", "이미 좋아요를 눌렀습니다."),
    DUPLICATE_DISLIKE(HttpStatus.BAD_REQUEST, "LikeDislike_4003","이미 싫어요를 눌렀습니다." ),
    LIKE_DISLIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LIKEDISLIKE_4001", "해당 좋아요/싫어요를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return getReason();
    }

}
