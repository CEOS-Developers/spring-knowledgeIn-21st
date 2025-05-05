package com.ceos21.knowledgeIn.exception.dto;

import com.ceos21.knowledgeIn.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Builder
public class ErrorResponseEntity {
    private int statusCode;     //ex. 400, 404
    private String message;
    private String codeName;    //ex. THIS_IS_EXAMPLE

    public static ResponseEntity<ErrorResponseEntity> of(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                .statusCode(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .codeName(errorCode.name())
                .build());
    }
}
