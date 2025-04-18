package com.ceos21.spring_knowledgeIn_21st.domain.post.dto.request;

import lombok.Getter;

@Getter
public class PostUserRequest {
    private Long userId;
    public PostUserRequest(Long postId, Long userId) {
        this.userId = userId;
    }
}
