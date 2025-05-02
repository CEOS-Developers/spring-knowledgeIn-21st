package com.ceos21.knowledgein.post.exception;

import com.ceos21.knowledgein.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    POST_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "게시글 제목이 비어있습니다."),
    POST_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "게시글 내용이 비어있습니다."),
    SAVE_IMAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다. 다시 시도해 주세요"),
    STORAGE_FULL(HttpStatus.INSUFFICIENT_STORAGE, "저장소가 가득 찼습니다. 이미지를 저장할 수 없습니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
