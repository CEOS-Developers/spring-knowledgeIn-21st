package com.ceos21.ceos21BE.web.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class HashtagPostResponseDTO {
    private Long postId;
    private String title;
    private String username;
    private LocalDateTime createdAt;
}
