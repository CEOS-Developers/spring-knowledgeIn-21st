package com.ceos21.spring_knowledgeIn_21st.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // CommonError
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_ACCESS(HttpStatus.FORBIDDEN, "Invalid access"),
    CONTENT_TOO_SHORT(HttpStatus.BAD_REQUEST, "내용은 최소 1자 이상이어야 합니다"),

    // PostError
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not exists"),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Only the author is allowed to modify or delete this post."),

    // HashtagError
    HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Hashtag not found"),

    // UserError
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not exists"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Email already exists"),

    // AnswerError
    CANNOT_ANSWER_OWN_POST(HttpStatus.FORBIDDEN, "You cannot answer your own post."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "Answer not exists"),
    ANSWER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Only the author is allowed to modify or delete this answer."),

    // TokenError
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "Invalid JWT signature"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired JWT token"),
    UNSUPRORTED_TOKEN(HttpStatus.UNAUTHORIZED, "Unsupported JWT token"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT token"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token is invalid or mismatched"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh token not found in server"),

    // CommentError
    COMMENT_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "Only the author is allowed to modify or delete this comment."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not exists"),

    ;
    private final HttpStatus status;
    private final String message;
}