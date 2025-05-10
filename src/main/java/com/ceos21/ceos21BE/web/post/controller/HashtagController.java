package com.ceos21.ceos21BE.web.post.controller;

import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.web.post.dto.response.HashtagPostResponseDTO;
import com.ceos21.ceos21BE.web.post.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HashtagController {

    private final HashtagService hashtagService;

    // 해시태그 별 질문 조회
    @GetMapping("/hashtags/{name}/posts")
    public ApiResponse<List<HashtagPostResponseDTO>> getPostsByHashtag(
            @PathVariable String name) {
        List<HashtagPostResponseDTO> posts = hashtagService.getPostsByHashtag(name);
        return ApiResponse.onSuccess(posts);
    }

    // Hashtag 목록 조회
    @GetMapping("/hashtags")
    public ApiResponse<List<String>> getAllHashtags() {
        List<String> hashtags = hashtagService.getAllHashtags();
        return ApiResponse.onSuccess(hashtags);
    }


}
