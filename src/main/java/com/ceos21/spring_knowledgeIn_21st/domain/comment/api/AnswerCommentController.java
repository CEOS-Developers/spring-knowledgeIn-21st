package com.ceos21.spring_knowledgeIn_21st.domain.comment.api;

import com.ceos21.spring_knowledgeIn_21st.domain.comment.application.AnswerCommentService;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.AnswerComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.request.CommentAddRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.request.CommentUpdateRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.response.CommentResponse;
import com.ceos21.spring_knowledgeIn_21st.global.common.ApiResponse;
import com.ceos21.spring_knowledgeIn_21st.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AnswerCommentController {
    private final AnswerCommentService answerCommentService;

    @GetMapping("/answers/{answerId}/comments")
    @Operation(
            summary = "답변 댓글 조회",
            description = "답변의 전체 댓글을 조회합니다"
    )
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getAllComments(
            @PathVariable Long answerId
    ) {
        List<AnswerComment> comments = answerCommentService.findAllComments(answerId);
        if (comments.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(List.of(), "등록된 댓글이 없습니다"));
        }
        List<CommentResponse> responses = comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping("/answers/{answerId}/comments")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "답변 댓글 추가",
            description = "답변에 댓글을 등록합니다"
    )
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @RequestBody CommentAddRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long answerId
    ){
        AnswerComment comment = answerCommentService.addComment(request, answerId, userDetails.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(CommentResponse.from(comment), "댓글이 등록되었습니다"));
    }

    @PostMapping("/answers/{answerId}/comments/{commentId}")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "답변 댓글 수정",
            description = "답변의 댓긍을 수정합니다"
    )
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @RequestBody CommentUpdateRequest request,
            @PathVariable Long answerId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        AnswerComment comment = answerCommentService.updateComment(request, answerId, commentId, userDetails.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommentResponse.from(comment)));
    }

    @DeleteMapping("/answers/{answerId}/comments/{commentId}")
    @Operation(
            summary = "댓글 삭제",
            description = "댓글을 삭제합니다"
    )
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long answerId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        answerCommentService.deleteComment(answerId, commentId, userDetails.getUser().getId());
        return ResponseEntity
                .ok(ApiResponse.success(null, "댓글이 삭제되었습니다"));
    }
}
