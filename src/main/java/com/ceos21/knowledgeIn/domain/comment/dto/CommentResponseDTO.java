package com.ceos21.knowledgeIn.domain.comment.dto;

import com.ceos21.knowledgeIn.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentResponseDTO {
    private Long commentId;
    private Long postId;
    private String writer; //닉네임
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponseDTO from(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .writer(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
