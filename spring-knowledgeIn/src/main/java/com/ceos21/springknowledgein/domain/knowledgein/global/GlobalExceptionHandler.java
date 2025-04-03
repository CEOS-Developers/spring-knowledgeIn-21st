package com.ceos21.springknowledgein.domain.knowledgein.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice //RestController가 달린 컨트롤러 전역 예외처리
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class) // 런타임 에러에 대해 다음 예외 처리 실행
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
