package com.ceos21.ceos21BE.web.post.dto.response;

import com.ceos21.ceos21BE.web.post.entity.PostType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponseDto {
    private Long postId;

    // 질문일 경우만
    private String title;

    private String content;

    private Long userId;

    private String username;

    private List<String> hashtags;

    private List<String> imageUrls;

    private PostType postType;

    private Long parentId; // 답변일 경우만
}
