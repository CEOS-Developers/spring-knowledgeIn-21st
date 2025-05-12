package com.ceos21.ceos21BE.web.comment.controller;

import com.ceos21.ceos21BE.web.user.customDetail.CustomDetails;
import com.ceos21.ceos21BE.global.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.global.apiPayload.code.status.SuccessStatus;
import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponseDTO;
import com.ceos21.ceos21BE.web.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<CommentResponseDTO> createComment(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomDetails user,
            @RequestParam String content) {
        CommentResponseDTO response = commentService.createComment(postId, user.getUser().getUserId(), content);
        return ApiResponse.of(SuccessStatus._CREATED, response);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomDetails user) {
        commentService.deleteComment(commentId, user.getUser().getUserId());
        return ApiResponse.of(SuccessStatus._OK, null);
    }

    // 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<List<CommentResponseDTO>> getComment(@PathVariable Long postId) {
        List<CommentResponseDTO> response = commentService.getCommentByPostId(postId);
        return ApiResponse.onSuccess(response);
    }

}
