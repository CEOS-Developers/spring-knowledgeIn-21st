package com.ceos21.knowledgeIn.controller.dto.post;

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
