package com.knowledgein.springboot.service.postService;

import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.User;
import com.knowledgein.springboot.web.dto.postDTO.PostRequestDTO;
import com.knowledgein.springboot.web.dto.postDTO.PostResponseDTO;

import java.util.List;

public interface PostCommandService {
    Post createPost(PostRequestDTO.CreateDto request, User user);
    List<PostResponseDTO.ResultDto> deletePost(Long postId, User user);
    Post updatePost(Long postId, User user, PostRequestDTO.UpdateDto request);
}
