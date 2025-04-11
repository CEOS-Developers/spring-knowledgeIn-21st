package com.ceos21.ceos21BE.web.comment.controller;

import com.ceos21.ceos21BE.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.web.comment.dto.request.CommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.request.DeleteCommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponse;
import com.ceos21.ceos21BE.web.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create/{postId}")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestParam String content) {
        CommentRequest request = CommentRequest.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .build();
        return ApiResponse.onSuccess(commentService.createComment(request));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        DeleteCommentRequest request = DeleteCommentRequest.builder()
                .commentId(commentId)
                .userId(userId)
                .build();
        return ApiResponse.onSuccess(commentService.deleteComment(request));
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<CommentResponse>> getCommentByUserId(@PathVariable Long userId) {
        return ApiResponse.onSuccess(commentService.getCommentByUSerId(userId));
    }

}
