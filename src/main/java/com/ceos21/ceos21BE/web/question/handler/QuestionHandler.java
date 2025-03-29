package com.ceos21.ceos21BE.web.question.handler;

import com.ceos21.ceos21BE.apiPayload.code.BaseErrorCode;
import com.ceos21.ceos21BE.apiPayload.exception.GeneralException;

public class QuestionHandler extends GeneralException {

    public QuestionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
