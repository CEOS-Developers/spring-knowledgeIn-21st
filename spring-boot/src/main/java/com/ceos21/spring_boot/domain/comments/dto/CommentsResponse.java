package com.ceos21.spring_boot.domain.comments.dto;

import com.ceos21.spring_boot.domain.comments.Comments;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentsResponse {

    private Long id;
    private String texts;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentsResponse from(Comments comment) {
        CommentsResponse response = new CommentsResponse();
        response.id = comment.getId();
        response.texts = comment.getTexts();
        response.userId = comment.getUser().getId();
        response.postId = comment.getPost().getId();
        response.createdAt = comment.getCreatedAt();
        response.updatedAt = comment.getUpdatedAt();
        return response;
    }
}
