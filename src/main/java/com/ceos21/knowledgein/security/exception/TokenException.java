package com.ceos21.knowledgein.security.exception;

import com.ceos21.knowledgein.global.exception.CustomException;

public class TokenException extends CustomException {

    public TokenException(SecurityErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(SecurityErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
