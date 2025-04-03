package com.ceos21.knowledgeIn.global.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private Status status;

    public HttpStatus getHttpStatus() {
        return this.status.getStatus();
    }

    public String getCode(){
        return this.status.getCode();
    }

    public String getMessage(){
        return this.status.getMessage();
    }
}
