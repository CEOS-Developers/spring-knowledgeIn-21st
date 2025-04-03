package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.Comment;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Builder
@Data
public class CommentCreateDTO {

    private String content;

    private Long userId;

    private Long postId;

}
