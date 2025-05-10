package com.ceos21.ceos21BE.web.user.handler;

import com.ceos21.ceos21BE.global.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
