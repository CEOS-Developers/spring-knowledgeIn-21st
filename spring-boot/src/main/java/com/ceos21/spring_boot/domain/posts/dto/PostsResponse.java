package com.ceos21.spring_boot.domain.posts.dto;

import com.ceos21.spring_boot.domain.posts.Posts;
import lombok.Getter;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostsResponse {
    private Long id;
    private String pictures;
    private String texts;
    private String tags;
    private String comment;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static PostsResponse from(Posts post) {
        return PostsResponse.builder()
                .id(post.getId())
                .pictures(post.getPictures())
                .texts(post.getTexts())
                .tags(post.getTags())
                .comment(post.getComment())
                .userId(post.getUser().getId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
