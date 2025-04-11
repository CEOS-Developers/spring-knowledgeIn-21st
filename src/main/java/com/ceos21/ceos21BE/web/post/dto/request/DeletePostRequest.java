package com.ceos21.ceos21BE.web.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeletePostRequest {
    private Long postId;
    private Long userId;
}
