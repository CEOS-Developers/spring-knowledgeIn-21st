package com.ceos21.knowledgeIn.global.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private ErrorStatus errorStatus;

    public HttpStatus getHttpStatus() {
        return this.errorStatus.getStatus();
    }

    public String getCode(){
        return this.errorStatus.getCode();
    }

    public String getMessage(){
        return this.errorStatus.getMessage();
    }
}
