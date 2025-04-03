package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.PostHashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PostHashTagResponseDTO {
    private Long postHashTagId;

    private Long hashTagId;

    private Long postId;

    public static PostHashTagResponseDTO from(PostHashTag postHashTag) {
        return PostHashTagResponseDTO.builder()
                .postHashTagId(postHashTag.getId())
                .hashTagId(postHashTag.getId())
                .postId(postHashTag.getPost().getId())
                .build();
    }

}
