package com.ceos21.spring_boot.DTO.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class PostRequestDTO {
    private final String title;
    private final String content;
    private final List<String> imageUrls;
    private final List<String> hashtags;
}
