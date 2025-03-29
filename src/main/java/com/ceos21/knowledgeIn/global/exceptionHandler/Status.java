package com.ceos21.knowledgeIn.global.exceptionHandler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Status {

    /**
     *  200 Request 성공
     */
    SUCCESS(HttpStatus.OK,"COMMON200","요청에 성공하였습니다."),

    /**
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON4000","잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON4001","해당 데이터를 찾을 수 없습니다."),
    ALREADY_EXISTS(HttpStatus.CONFLICT,"COMMON4002", "이미 존재하는 값입니다."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4100", "해당 리소스에 접근 권한이 없습니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON500","서버 에러, 관리자에게 문의 바랍니다."),
    /**
     * 503 Service Unavailable Error
     */


    /**
     * 커스텀 에러
     */
    // 멤버
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT,"MEMBER4000","이미 존재하는 회원입니다."),

    //게시글
    QUESTION_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN,"POST4000","답변이 존재하는 질문은 삭제할 수 없습니다."),
    QUESTION_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN,"POST4001","답변이 존재하는 질문은 수정할 수 없습니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;

}
