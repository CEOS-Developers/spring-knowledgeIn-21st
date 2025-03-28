package com.ceos21.knowledgeIn.global.exceptionHandler;


import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //유효성 검사 실패(@NotNull, @Size 등 제약 조건 위반과 @RequestParam, @PathVariable 검증 실패) 시 처리
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> validation (ConstraintViolationException e, WebRequest request) {

        ErrorStatus errorStatus = ErrorStatus.BAD_REQUEST;

        ApiResponse<Object> body = ApiResponse.onFailure(errorStatus.getCode(),
                errorStatus.getMessage()+"\n제약조건에 위배되는 값이 포함되어 있습니다.",null);

        return handleExceptionInternal(e, body, HttpHeaders.EMPTY, errorStatus.getStatus(), request);
    }

    //@Valid 어노테이션을 이용한 검증에 실패 시, 또는 @RequestBody 로 들어오는 객체 검증 실패 시
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorStatus errorStatus = ErrorStatus.BAD_REQUEST;

        Map<String, String> errors = new LinkedHashMap<>(); //HashMap 과 달리 입력 순서대로 key 가 보장됨

        e.getBindingResult().getFieldErrors().stream()  //유효성 검증에 실패한 필드를 순회하며 각 필드와 메시지를 errors Map에 저장
                .forEach(fieldError -> {
                    String field = fieldError.getField();
                    String message = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    //같은 필드가 여러개의 메시지를 가질 경우 쉼표로 구분하여 병합
                    errors.merge(field, message, (oldField, newField) -> oldField + ", "+ newField);
                });

        ApiResponse<Object> body = new ApiResponse<>(false, errorStatus.getCode(), errorStatus.getMessage(),errors);

        return handleExceptionInternal(e, body, headers, errorStatus.getStatus(), request);
    }

    //커스텀 예외인 General Exception 의 예외 처리
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException e, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(e.getCode(),e.getMessage(),null);

        return super.handleExceptionInternal(e,body,HttpHeaders.EMPTY,e.getHttpStatus(),request);
    }


    //처리되지 않은 모든 예외를 500 Internal Server Error 로 반환, 단 응답 통일을 위해 선언
    @ExceptionHandler
    public ResponseEntity<Object> handleGeneralException(Exception e, WebRequest request) {
        e.printStackTrace(); //에러 출력

        ErrorStatus errorStatus = ErrorStatus.INTERNAL_SERVER_ERROR;

        ApiResponse<Object> body = ApiResponse.onFailure(errorStatus.getCode(),
                errorStatus.getMessage(), e.getMessage());

        //ResponseEntity 를 만들어주는 부모 클래스 메서드
        return super.handleExceptionInternal(
                e,
                body,
                HttpHeaders.EMPTY,
                errorStatus.getStatus(),
                request
        );
    }


}
