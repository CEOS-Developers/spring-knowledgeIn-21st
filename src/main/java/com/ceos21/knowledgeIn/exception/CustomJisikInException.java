package com.ceos21.knowledgeIn.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomJisikInException extends RuntimeException{
    private ErrorCode errorCode;

    public CustomJisikInException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CustomJisikInException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
