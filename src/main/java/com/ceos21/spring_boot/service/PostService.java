package com.ceos21.spring_boot.service;

import com.ceos21.spring_boot.dto.Answer.AnswerRequestDTO;
import com.ceos21.spring_boot.dto.Answer.AnswerResponseDTO;
import com.ceos21.spring_boot.dto.post.PostRequestDTO;
import com.ceos21.spring_boot.dto.post.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostResponseDTO addPost(PostRequestDTO postRequest);

    List<PostResponseDTO> getPostsByUserId(Long userId);

    void deletePost(Long postId, Long userId);
}
