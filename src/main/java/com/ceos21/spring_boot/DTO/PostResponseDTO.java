package com.ceos21.spring_boot.DTO;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponseDTO {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final String createdAt;
    private final List<String> imageUrls;
    private final List<String> hashtags;
    private final List<AnswerDTO> answers;
}

