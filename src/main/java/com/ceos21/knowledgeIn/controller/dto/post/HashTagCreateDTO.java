package com.ceos21.knowledgeIn.controller.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class HashTagCreateDTO {
    private List<String> contents;

    private Long postId;
}
