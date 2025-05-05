package com.ceos21.knowledgein.security.exception;

import com.ceos21.knowledgein.global.exception.CustomException;
import com.ceos21.knowledgein.global.exception.ErrorCode;

public class SecurityImplementException extends CustomException {
    public SecurityImplementException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SecurityImplementException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
