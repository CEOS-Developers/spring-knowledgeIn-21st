package com.knowledgein.springboot.service.postService;

import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.web.dto.postDTO.PostRequestDTO;
import com.knowledgein.springboot.web.dto.postDTO.PostResponseDTO;

import java.util.List;

public interface PostCommandService {
    Post createPost(PostRequestDTO.CreateDto request);
    List<PostResponseDTO.ResultDto> deletePost(Long postId, Long userId);
    Post updatePost(Long postId, Long userId, PostRequestDTO.UpdateDto request);
}
