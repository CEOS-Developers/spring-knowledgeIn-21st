package com.ceos21.knowledgeIn.controller.post;

import com.ceos21.knowledgeIn.controller.dto.post.PostCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostModifyDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostResponseDTO;
import com.ceos21.knowledgeIn.service.post.HashTagService;
import com.ceos21.knowledgeIn.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {
    private final PostService postService;
    private final HashTagService hashTagService;

    /**
     * Post & HashTag (&PostHashTag) api
     * */
    @PostMapping("/posts")
    public Long createPost(@RequestBody PostCreateDTO dto) {
        Long postId = postService.createPost(dto);

        if (dto.getHashTagContents() != null) {
            hashTagService.createOrFindHashTags(dto.getHashTagContents(), postId);
        }

        return postId;
    }

    @GetMapping("/posts/{postId}")
    public PostResponseDTO findPost(@PathVariable Long postId) {
        PostResponseDTO dto = postService.findPost(postId);

        return dto;
    }

/*
    @GetMapping("/posts/hashTags")
    public List<PostResponseDTO> findAllPostsByHashTag(@RequestParam("hashTags") List<String> hashTags) {
        //...구현필요-> 개선
    }
*/


    @PutMapping("/posts/{postId}")
    public PostResponseDTO modifyPost(@PathVariable Long postId, @RequestBody PostModifyDTO dto) {
        return postService.modifyPost(dto, postId);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

}
