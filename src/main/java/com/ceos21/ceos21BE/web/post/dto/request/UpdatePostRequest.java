package com.ceos21.ceos21BE.web.post.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdatePostRequest {
    //private Long postId;
    private String title;
    private String content;
    private List<String> hashtags;
    private List<String> imageUrls;
}
