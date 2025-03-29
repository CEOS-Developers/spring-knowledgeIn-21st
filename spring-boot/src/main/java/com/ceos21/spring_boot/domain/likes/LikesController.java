package com.ceos21.spring_boot.domain.likes;

import com.ceos21.spring_boot.domain.likes.dto.LikesRequest;
import com.ceos21.spring_boot.domain.likes.dto.LikesResponse;
import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.posts.PostsService;
import com.ceos21.spring_boot.domain.users.Users;
import com.ceos21.spring_boot.domain.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final UsersService usersService;
    private final PostsService postsService;

    @PostMapping
    public LikesResponse createLike(@RequestBody LikesRequest request) {
        Users user = usersService.getUserById(request.getUserId());
        Posts post = postsService.getPostById(request.getPostId());
        return LikesResponse.from(likesService.createLike(request.toEntity(user, post)));
    }

    @GetMapping
    public List<LikesResponse> getAllLikes() {
        return likesService.getAllLikes()
                .stream()
                .map(LikesResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LikesResponse getLikeById(@PathVariable Long id) {
        return LikesResponse.from(likesService.getLikeById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable Long id) {
        likesService.deleteLike(id);
    }
}
