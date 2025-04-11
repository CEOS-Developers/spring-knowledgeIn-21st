package com.ceos21.ceos21BE.web.comment.converter;

import com.ceos21.ceos21BE.web.comment.dto.request.CommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponse;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public Comment toCommentEntity(String content, UserEntity user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }

    public CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .build();
    }
}
