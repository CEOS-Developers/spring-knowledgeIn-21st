package com.ceos21.knowledgein.global.handler;

import com.ceos21.knowledgein.global.dto.ResponseError;
import com.ceos21.knowledgein.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseError> handleCustomException(CustomException e,
                                                               HttpServletRequest request) {

        ResponseError responseError = new ResponseError();
        responseError.setMessageDetail(e.getMessage());
        responseError.setPath(request.getRequestURI());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(responseError);
    }

    // 요청 본문이 없거나 변환할 수 없는 경우 (NOT NULL)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                                     HttpServletRequest request) {

        ResponseError responseError = new ResponseError();
        responseError.setMessageDetail("요청 본문이 누락되었거나 올바르지 않습니다.");
        responseError.setErrorDetail(ex.getLocalizedMessage());
        responseError.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    //TODO: @Valid 검증 실패 예외처리
//    @ExceptionHandler(MethodArgumentNotValidException.class)


}