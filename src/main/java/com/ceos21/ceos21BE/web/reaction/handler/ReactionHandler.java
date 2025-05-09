package com.ceos21.ceos21BE.web.reaction.handler;

import com.ceos21.ceos21BE.global.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.global.apiPayload.exception.GeneralException;

public class ReactionHandler extends GeneralException {
    public ReactionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
