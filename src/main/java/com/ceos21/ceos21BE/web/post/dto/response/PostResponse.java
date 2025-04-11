package com.ceos21.ceos21BE.web.post.dto.response;

import com.ceos21.ceos21BE.web.post.entity.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private Long userId;
    private String username;
    private List<String> hashtags;
    private List<String> imageUrls;
    private PostType postType;
    private Long parentId; // 답변일 경우만
}
