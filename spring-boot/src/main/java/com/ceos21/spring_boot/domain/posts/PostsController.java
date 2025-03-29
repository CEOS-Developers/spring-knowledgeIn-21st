package com.ceos21.spring_boot.domain.posts;

import com.ceos21.spring_boot.domain.posts.dto.PostsRequest;
import com.ceos21.spring_boot.domain.posts.dto.PostsResponse;
import com.ceos21.spring_boot.domain.users.Users;
import com.ceos21.spring_boot.domain.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;
    private final UsersService usersService; 

    @PostMapping
    public PostsResponse createPost(@RequestBody PostsRequest request) {
        Users user = usersService.getUserById(request.getUserId());
        
        Posts savedPost = postsService.createPost(request.toEntity(user)); 
        return PostsResponse.from(savedPost);
        
    }

    @GetMapping
    public List<PostsResponse> getAllPosts() {
        return postsService.getAllPosts()
                .stream()
                .map(PostsResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostsResponse getPostById(@PathVariable Long id) {
        return PostsResponse.from(postsService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postsService.deletePost(id);
    }
}
