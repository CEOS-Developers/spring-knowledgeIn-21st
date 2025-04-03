package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@AllArgsConstructor
@Data
public class CommentResponseDTO {
    private Long id;

    private String content;

    private Long userId;

    private Long postId;

    public static CommentResponseDTO from(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .build();
    }

}
