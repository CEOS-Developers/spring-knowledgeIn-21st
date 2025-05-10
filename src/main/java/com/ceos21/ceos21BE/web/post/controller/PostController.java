package com.ceos21.ceos21BE.web.post.controller;

import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.web.user.customDetail.CustomDetails;
import com.ceos21.ceos21BE.global.apiPayload.code.status.SuccessStatus;
import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.request.DeletePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.response.AnswerSummaryResponseDto;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponseDto;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.post.service.questionservice.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping("/posts")
    public ApiResponse<PostResponseDto> createPost(
            @AuthenticationPrincipal CustomDetails user,
            @RequestBody CreatePostRequestDto request) {
        PostResponseDto response = postService.createPost(request, user.getUser().getUserId());
        return ApiResponse.of(SuccessStatus._CREATED, response);
    }

    // 게시글 수정
    @PutMapping("/posts/{postId}}")
    public ApiResponse<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDto request,
            @AuthenticationPrincipal CustomDetails user) {
        PostResponseDto response = postService.updatePost(request, user.getUser().getUserId(), postId);
        return ApiResponse.of(SuccessStatus._UPDATED, response);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ApiResponse<String> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomDetails user) {

        DeletePostRequest request = DeletePostRequest.builder()
                .postId(postId)
                .build();
        postService.deletePost(request, user.getUser().getUserId());
        return ApiResponse.of(SuccessStatus._DELETED, null);
    }

    // 게시글 조회
    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponseDto> getPostById(@PathVariable Long postId) {
        return ApiResponse.onSuccess(postService.getPost(postId));
    }

    // 질문 목록 조회 (페이징)
    @GetMapping("/posts")
    public ApiResponse<Page<PostResponseDto>> getPostList (
            @RequestParam(defaultValue = "QUESTION")PostType type,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ) {
        return ApiResponse.onSuccess(postService.getPostList(type, pageable));
    }


    // 답변 목록 조회
    @GetMapping("/posts/{questionId}/answers")
    public ApiResponse<List<AnswerSummaryResponseDto>> getAnswersByQuestion(@PathVariable Long questionId) {
        return ApiResponse.onSuccess(postService.getAnswersByQuestionId(questionId));
    }

}
