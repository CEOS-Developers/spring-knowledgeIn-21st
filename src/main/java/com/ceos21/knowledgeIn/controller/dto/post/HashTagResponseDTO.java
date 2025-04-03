package com.ceos21.knowledgeIn.controller.dto.post;

import com.ceos21.knowledgeIn.domain.post.HashTag;
import com.ceos21.knowledgeIn.domain.post.PostHashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Builder
@Data
public class HashTagResponseDTO {
    private Long id;
    private String content;

    public static HashTagResponseDTO fromPostHashTag(PostHashTag postHashTag) {
        HashTag hashTag = postHashTag.getHashTag();

        return HashTagResponseDTO.builder()
                .id(hashTag.getId())
                .content(hashTag.getContent())
                .build();
    }

    public static HashTagResponseDTO from(HashTag hashTag) {
        return HashTagResponseDTO.builder()
                .id(hashTag.getId())
                .content(hashTag.getContent())
                .build();

    }
}
