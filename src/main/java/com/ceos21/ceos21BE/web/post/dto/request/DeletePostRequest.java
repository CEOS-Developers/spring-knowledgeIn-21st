package com.ceos21.ceos21BE.web.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeletePostRequest {
    private Long postId;
    //private Long userId;
    // 이제 jwt 방식으로 인증 할 것임
}
