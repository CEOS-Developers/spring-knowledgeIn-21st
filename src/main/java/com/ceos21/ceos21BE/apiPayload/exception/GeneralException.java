package com.ceos21.ceos21BE.apiPayload.exception;

import com.ceos21.ceos21BE.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.apiPayload.code.ErrorReasonDTO;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private BaseErrorCode code;

    public GeneralException(String message) {
        super(message);
        this.code = null;
    }

    public GeneralException(BaseErrorCode code){
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
