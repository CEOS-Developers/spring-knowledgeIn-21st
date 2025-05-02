package com.ceos21.spring_boot.Controller.post;

import com.ceos21.spring_boot.DTO.auth.CustomUserDetailsDTO;
import com.ceos21.spring_boot.DTO.post.PostRequestDTO;
import com.ceos21.spring_boot.DTO.post.PostResponseDTO;
import com.ceos21.spring_boot.DTO.post.PostResponseWithAnswerDTO;
import com.ceos21.spring_boot.Domain.user.User;
import com.ceos21.spring_boot.Service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "게시글 API", description = "게시글 CRUD를 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @Operation(summary = "게시글 생성", description = "게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO postCreateDTO, @AuthenticationPrincipal CustomUserDetailsDTO userDetails) {

        User user = userDetails.getUser();

        PostResponseDTO postResponseDTO = postService.createPost(postCreateDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    // 특정 게시글 조회
    @Operation(summary = "특정 게시글 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseWithAnswerDTO> getPost(@PathVariable Long id) {
        PostResponseWithAnswerDTO postResponseDTO = postService.getPost(id);
        return ResponseEntity.ok(postResponseDTO);
    }

    // 전체 게시글 조회
    @Operation(summary = "전체 게시글 조회", description = "모든 게시글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 자신이 쓴 전체 게시글 조회
    @Operation(summary = "내가 쓴 게시글 목록 조회", description = "현재 로그인한 사용자가 작성한 모든 게시글을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<PostResponseDTO>> getMyPosts(@AuthenticationPrincipal CustomUserDetailsDTO userDetails) {
        User user = userDetails.getUser();
        List<PostResponseDTO> posts = postService.getMyPosts(user);
        return ResponseEntity.ok(posts);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO postUpdateDTO,@AuthenticationPrincipal CustomUserDetailsDTO userDetails) {

        User user = userDetails.getUser();

        PostResponseDTO postResponseDTO = postService.updatePost(id, postUpdateDTO, user);
        return ResponseEntity.ok(postResponseDTO);
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetailsDTO userDetails) {

        User user = userDetails.getUser();

        String message = postService.deletePost(id, user);
        return ResponseEntity.ok(message);
    }

}

