package com.ceos21.springknowledgein.domain.knowledgein.controller;

import com.ceos21.springknowledgein.domain.knowledgein.Dto.PostCreateDto;
import com.ceos21.springknowledgein.domain.knowledgein.repository.Post;
import com.ceos21.springknowledgein.domain.knowledgein.service.PostService;
import com.ceos21.springknowledgein.domain.user.repository.Member;
import com.ceos21.springknowledgein.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostCreateDto postCreateDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        Post post = postService.createPost(postCreateDto, member);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();

    }

}
