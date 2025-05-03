package com.ceos21.knowledgeIn.domain.comment;

import com.ceos21.knowledgeIn.domain.comment.dto.CommentRequestDTO;
import com.ceos21.knowledgeIn.domain.comment.dto.CommentResponseDTO;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.service.CustomUserDetailService;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostService;
import com.ceos21.knowledgeIn.global.exceptionHandler.ApiResponse;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final PostService postService;
    private final CustomUserDetailService customUserDetailService;
    private final CommentService commentService;


    //댓글 생성
    @PostMapping("/{postId}")
    @Operation(summary = "특정 질문에 댓글 등록 API", description = "특정 질문에 댓글을 등록하는 API")
    public ApiResponse<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO.create requestDTO,
                                                         @PathVariable Long postId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {

        Member member = customUserDetailService.findMemberByUserDetails(userDetails);
        Post post = postService.getPost(postId);

        return ApiResponse.onSuccess(commentService.createComment(member,post,requestDTO));
    }

    //댓글 조회
    @GetMapping("/{postId}")
    @Operation(summary = "특정 게시글의 댓글 리스트 조회 API", description = "특정 게시글에 달린 댓글 목록을 조회하는 API")
    public ApiResponse<Page<CommentResponseDTO>> updateComment(@PathVariable Long postId,
                                                               @ParameterObject Pageable pageable) {
        Post post = postService.getPost(postId);

        return ApiResponse.onSuccess(commentService.getCommentList(post,pageable));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    @Operation(summary = "특정 댓글 삭제 API", description = "특정 댓글을 삭제하는 API")
    public ApiResponse<Object> updateComment(@PathVariable Long commentId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Member member = customUserDetailService.findMemberByUserDetails(userDetails);

        commentService.deleteComment(member, commentId);

        return new ApiResponse<>(true, Status.SUCCESS.getCode(),"삭제에 성공했습니다.",null);
    }
}
