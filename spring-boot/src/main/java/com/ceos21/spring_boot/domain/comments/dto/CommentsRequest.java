package com.ceos21.spring_boot.domain.comments.dto;

import com.ceos21.spring_boot.domain.comments.Comments;
import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.users.Users;
import lombok.Getter;

@Getter
public class CommentsRequest {

    private String texts;
    private Long userId;
    private Long postId;

    public Comments toEntity(Users user, Posts post) {
        return Comments.builder()
                .texts(texts)
                .user(user)
                .post(post)
                .build();
    }
}
