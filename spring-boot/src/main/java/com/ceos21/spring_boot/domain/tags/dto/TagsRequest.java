package com.ceos21.spring_boot.domain.tags.dto;

import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.tags.Tags;
import lombok.Getter;

@Getter
public class TagsRequest {

    private String tag;
    private Long postId;

    public Tags toEntity(Posts post) {
        return Tags.builder()
                .tag(tag)
                .post(post)
                .build();
    }
}
