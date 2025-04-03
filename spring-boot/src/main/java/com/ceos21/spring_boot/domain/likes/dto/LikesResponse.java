package com.ceos21.spring_boot.domain.likes.dto;

import com.ceos21.spring_boot.domain.likes.Likes;
import com.ceos21.spring_boot.domain.likes.TypeOfLikes;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LikesResponse {

    private Long id;
    private Long userId;
    private Long postId;
    private TypeOfLikes typeOfLike;
    private LocalDateTime createdAt;

    public static LikesResponse from(Likes like) {
        LikesResponse response = new LikesResponse();
        response.id = like.getId();
        response.userId = like.getUser().getId();
        response.postId = like.getPost().getId();
        response.typeOfLike = like.getTypeOfLike();
        response.createdAt = like.getCreatedAt();
        return response;
    }
}
