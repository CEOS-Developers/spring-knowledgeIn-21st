package com.ceos21.spring_knowledgeIn_21st.domain.post.api;

import com.ceos21.spring_knowledgeIn_21st.domain.post.application.PostService;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.request.PostAddRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.request.PostUpdateRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.response.PostResponse;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dto.response.PostInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Long> savePost(@RequestBody PostAddRequest postAddRequest) {
        Post savedPost = postService.savePost(postAddRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedPost.getId());
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest request) {
        postService.updatePost(postId, request);
        PostResponse response = new PostResponse(postId, "게시글이 수정되었습니다.", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostInfoResponse>> getAllPosts() {
        List<Post> posts = postService.findPosts(); // 모든 게시글 가져오기
        List<PostInfoResponse> response = posts.stream()
                .map(PostInfoResponse::new) // 각 Post를 PostAddResponse로 변환
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostInfoResponse> getPost(@PathVariable Long postId) {
        Post post = postService.findPostById(postId);
        PostInfoResponse response = new PostInfoResponse(post);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long postId, @RequestBody PostAddRequest request) {   // Spring Security의 @AuthenticationPrincipal 사용 예정
        postService.deletePost(postId, request.userId());
        PostResponse response = new PostResponse(postId, "게시글이 삭제되었습니다.", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
