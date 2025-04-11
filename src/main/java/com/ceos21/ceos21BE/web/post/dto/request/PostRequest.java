package com.ceos21.ceos21BE.web.post.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PostRequest {
    private String title;
    private String content;
    private List<String> hashtags;
    private List<String> imageUrls;
}
