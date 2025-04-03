package com.ceos21.spring_boot.domain.likes.dto;

import com.ceos21.spring_boot.domain.likes.Likes;
import com.ceos21.spring_boot.domain.likes.TypeOfLikes;
import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.users.Users;
import lombok.Getter;

@Getter
public class LikesRequest {

    private Long userId;
    private Long postId;
    private TypeOfLikes typeOfLike;

    public Likes toEntity(Users user, Posts post) {
        return Likes.builder()
                .user(user)
                .post(post)
                .typeOfLike(typeOfLike)
                .build();
    }
}
