package com.ceos21.ceos21BE.web.post.handler;

import com.ceos21.ceos21BE.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.apiPayload.exception.GeneralException;

public class PostHandler extends GeneralException {

    public PostHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
