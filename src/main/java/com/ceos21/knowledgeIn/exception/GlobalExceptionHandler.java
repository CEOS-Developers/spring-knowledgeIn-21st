package com.ceos21.knowledgeIn.exception;

import com.ceos21.knowledgeIn.exception.dto.ErrorResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomJisikInException.class)
    public ResponseEntity<ErrorResponseEntity> handleCustomJisikInException(CustomJisikInException e) {
        log.error("CustomJisikInException 발생: 상태코드={}, 메시지={}, 에러명={}",
                e.getErrorCode().getHttpStatus().value(),
                e.getErrorCode().getMessage(),
                e.getErrorCode().name());
        return ErrorResponseEntity.of(e.getErrorCode());
    }

}
