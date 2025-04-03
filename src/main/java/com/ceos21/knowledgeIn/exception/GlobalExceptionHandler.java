package com.ceos21.knowledgeIn.exception;

import com.ceos21.knowledgeIn.exception.dto.ErrorResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomJisikInException.class)
    public ResponseEntity<ErrorResponseEntity> handleCustomJisikInException(CustomJisikInException e) {
        return ErrorResponseEntity.of(e.getErrorCode());
    }

}
