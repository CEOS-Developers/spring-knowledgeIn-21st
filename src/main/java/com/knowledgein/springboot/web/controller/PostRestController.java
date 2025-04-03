package com.knowledgein.springboot.web.controller;

import com.knowledgein.springboot.apiPayload.ApiResponse;
import com.knowledgein.springboot.converter.PostConverter;
import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.service.postService.PostCommandService;
import com.knowledgein.springboot.service.postService.PostQueryService;
import com.knowledgein.springboot.web.dto.postDTO.PostRequestDTO;
import com.knowledgein.springboot.web.dto.postDTO.PostResponseDTO;
import com.knowledgein.springboot.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="게시물", description = "게시물 관련 API")
@RestController
@RequiredArgsConstructor
public class PostRestController {
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @PostMapping("/posts")
    @Operation(summary = "게시물 생성 API",
            description = "게시물에 필요한 내용을 작성해서 생성하는 API")
    public ApiResponse<PostResponseDTO.ResultDto> create(@RequestBody PostRequestDTO.CreateDto request) {
        Post post = postCommandService.createPost(request);
        return ApiResponse.onSuccess(PostConverter.toResultDto(post));
    }

    @GetMapping("/posts")
    @Operation(summary = "게시물 리스트 조회 API",
            description = "지정된 페이지의 게시물 리스트를 조회하는 API")
    public ApiResponse<PostResponseDTO.PreviewListDto> getPage(@CheckPage @RequestParam(name = "page") Integer page) {
        Integer pageNumber = page - 1;

        Page<Post> postPage = postQueryService.getPostPage(pageNumber);
        return ApiResponse.onSuccess(PostConverter.toPreviewListDto(postPage));
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "특정 게시물 조회 API",
            description = "지정된 게시물을 조회하는 API")
    public ApiResponse<PostResponseDTO.PreviewDto> get(@PathVariable(name = "postId") Long postId) {
        Post post = postQueryService.getPost(postId);
        return ApiResponse.onSuccess(PostConverter.toPreviewDto(post));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "특정 게시물 삭제 API",
            description = "지정된 게시물을 삭제하는 API")
    public ApiResponse<List<PostResponseDTO.ResultDto>> delete(@PathVariable(name = "postId") Long postId, @RequestParam(name = "userId") Long userId) {
        return ApiResponse.onSuccess(postCommandService.deletePost(postId, userId));
    }

    @PatchMapping("/posts/{postId}")
    @Operation(summary = "특정 게시물 일부 수정 API",
            description = "지정된 게시물의 일부 필드를 수정하는 API")
    public ApiResponse<PostResponseDTO.UpdatedDto> update(@PathVariable(name = "postId") Long postId, @RequestParam(name = "userId") Long userId, @RequestBody PostRequestDTO.UpdateDto request) {
        Post updatedPost = postCommandService.updatePost(postId, userId, request);
        return ApiResponse.onSuccess(PostConverter.toUpdatedDto(updatedPost));
    }
}
