package com.ceos21.ceos21BE.global.apiPayload.code.status;

import com.ceos21.ceos21BE.global.apiPayload.code.BaseCode;
import com.ceos21.ceos21BE.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),
    _UPDATED(HttpStatus.OK, "COMMON202", "수정되었습니다."),
    _DELETED(HttpStatus.OK, "COMMON204", "삭제되었습니다."),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason(){
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build()
                ;
    }

    @Override
    public ReasonDTO getReasonHttpStatus(){
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
