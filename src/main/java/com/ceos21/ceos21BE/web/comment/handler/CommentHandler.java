package com.ceos21.ceos21BE.web.comment.handler;

import com.ceos21.ceos21BE.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.apiPayload.exception.GeneralException;

public class CommentHandler extends GeneralException {
    public CommentHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

