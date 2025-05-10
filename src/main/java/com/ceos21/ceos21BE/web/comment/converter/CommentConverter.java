package com.ceos21.ceos21BE.web.comment.converter;

import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponseDTO;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public Comment toCommentEntity(String content, User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }

    public CommentResponseDTO toCommentResponse(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .build();
    }
}
