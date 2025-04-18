package com.ceos21.spring_knowledgeIn_21st.domain.post.dto.response;

import lombok.Getter;

@Getter
public class PostResponse {
    private Long postId;
    private String message;
    private boolean success;

    public PostResponse(Long postId, String message, boolean success) {
        this.postId = postId;
        this.message = message;
        this.success = success;
    }
}
