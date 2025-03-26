package com.ceos21.knowledgein.post.exception;

import com.ceos21.knowledgein.global.exception.CustomException;

public class PostException extends CustomException {

    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }

    public PostException(PostErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
