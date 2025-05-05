package com.ceos21.ceos21BE.web.post.controller;

import com.ceos21.ceos21BE.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.customDetail.CustomDetails;
import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.DeletePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponse;
import com.ceos21.ceos21BE.web.post.service.questionservice.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping("/create/{userId}")
    public ApiResponse<PostResponse> createPost(
            @PathVariable Long userId,
            @RequestBody CreatePostRequest request) {
        return ApiResponse.onSuccess(postService.createPost(request, userId));
    }

    @PutMapping("/update/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request,
            @RequestParam Long userId) {
        return ApiResponse.onSuccess(postService.updatePost(request, userId, postId));
    }

    @DeleteMapping("/delete/{postId}")
    public ApiResponse<String> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomDetails customDetails) {

        DeletePostRequest request = DeletePostRequest.builder()
                .postId(postId)
                .build();
        postService.deletePost(request, customDetails.getUsername());
        return ApiResponse.onSuccess("삭제 성공");
    }

    @GetMapping("/hashtag/{hashtag}")
    public ApiResponse<List<PostResponse>> getPostByHashtag(@PathVariable String hashtag) {
        return ApiResponse.onSuccess(postService.getPostsByHashtag(hashtag));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<PostResponse>> getPostByUser(@PathVariable Long userId) {
        return ApiResponse.onSuccess(postService.getPostsByUser(userId));
    }

    @GetMapping("/all")
    public ApiResponse<List<PostResponse>> getAllPosts() {
        return ApiResponse.onSuccess(postService.getAllPosts());
    }


}
