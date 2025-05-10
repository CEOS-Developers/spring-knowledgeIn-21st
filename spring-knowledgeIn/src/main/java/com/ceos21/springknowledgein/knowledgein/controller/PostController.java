package com.ceos21.springknowledgein.knowledgein.controller;

import com.ceos21.springknowledgein.knowledgein.dto.PostCreateDto;
import com.ceos21.springknowledgein.knowledgein.domain.Post;
import com.ceos21.springknowledgein.knowledgein.service.PostService;
import com.ceos21.springknowledgein.user.repository.Member;
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
