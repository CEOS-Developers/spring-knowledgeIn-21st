package com.ceos21.spring_knowledgeIn_21st.domain.post.api;

import com.ceos21.spring_knowledgeIn_21st.domain.post.application.PostService;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.request.PostAddRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.request.PostUpdateRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.response.PostResponse;
import com.ceos21.spring_knowledgeIn_21st.global.common.ApiResponse;
import com.ceos21.spring_knowledgeIn_21st.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "게시글 추가",
            description = "새로운 게시글을 등록합니다"
    )
    public ResponseEntity<ApiResponse<PostResponse>> savePost(
            @RequestBody PostAddRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        Post savedPost = postService.savePost(request, userDetailsImpl.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(PostResponse.from(savedPost)));
    }

    @GetMapping("/posts")
    @Operation(
            summary = "게시글 전체 조회",
            description = "등록된 전체 게시글을 조회합니다"
    )
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<Post> posts = postService.findPosts(); // 모든 게시글 가져오기
        List<PostResponse> response = posts.stream()
                .map(PostResponse::from) // 각 Post를 PostAddResponse로 변환
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "(특정) 게시글 조회", description = "등록된 하나의 게시글을 조회합니다")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        Post post = postService.findPostById(postId);
        PostResponse response = PostResponse.from(post);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "(특정) 게시글 삭제", description = "등록된 하나의 게시글을 삭제합니다")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
            ) {
        postService.deletePost(postId, userDetailsImpl.getUserId());
        return ResponseEntity
                .ok(ApiResponse.success(null)); // 200 OK + body 포함

    }

    @PatchMapping("/posts/{postId}")
    @SecurityRequirement(name = "Authorization")
    @Operation(
            summary = "게시글 수정",
            description = "게시글의 제목과 내용을 수정합니다"
    )
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
    ) {
        Post savedPost = postService.updatePost(postId, request, userDetailsImpl.getUserId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(PostResponse.from(savedPost)));
    }

    @GetMapping(value = "/posts", params = "hashtag")
    @Operation(
            summary = "해시태그를 통한 게시글 조회",
            description = "해당 해시태그를 가진 게시글을 모두 조회합니다"
    )
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsByHashtag(@RequestParam String hashtag) {
        List<Post> posts = postService.findPostByHashtag(hashtag);
        List<PostResponse> response = posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
