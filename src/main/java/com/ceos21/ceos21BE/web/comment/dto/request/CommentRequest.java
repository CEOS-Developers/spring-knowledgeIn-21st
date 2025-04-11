package com.ceos21.ceos21BE.web.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequest {
    private Long userId;
    private Long postId;
    private String content;
}
