package com.ceos21.spring_boot.domain.posts.dto;

import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.users.Users;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class PostsRequest {

    private String pictures;
    private String texts;
    private String tags;
    private String comment;
    private Long userId;

    public Posts toEntity(Users user) {
        return Posts.builder()
                .pictures(pictures)
                .texts(texts)
                .tags(tags)
                .comment(comment)
                .user(user)
                .build();
    }
}
