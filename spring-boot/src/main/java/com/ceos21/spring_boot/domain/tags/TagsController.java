package com.ceos21.spring_boot.domain.tags;

import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.posts.PostsService;
import com.ceos21.spring_boot.domain.tags.dto.TagsRequest;
import com.ceos21.spring_boot.domain.tags.dto.TagsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;
    private final PostsService postsService;

    @PostMapping
    public TagsResponse createTag(@RequestBody TagsRequest request) {
        Posts post = postsService.getPostById(request.getPostId());
        return TagsResponse.from(tagsService.createTag(request.toEntity(post)));
    }

    @GetMapping
    public List<TagsResponse> getAllTags() {
        return tagsService.getAllTags()
                .stream()
                .map(TagsResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TagsResponse getTagById(@PathVariable Long id) {
        return TagsResponse.from(tagsService.getTagById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagsService.deleteTag(id);
    }
}
