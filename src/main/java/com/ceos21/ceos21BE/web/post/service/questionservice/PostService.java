package com.ceos21.ceos21BE.web.post.service.questionservice;

import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.request.DeletePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequestDto;
import com.ceos21.ceos21BE.web.post.dto.response.AnswerSummaryResponseDto;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponseDto;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;


public interface PostService {
    // 게시글 생성 (질문 or 답변)
    PostResponseDto createPost(CreatePostRequestDto request, Long userId);

    // 게시글 수정
    PostResponseDto updatePost(UpdatePostRequestDto request, Long userId, Long postId);

    // 게시글 삭제
    void deletePost(DeletePostRequest request, Long userId);

    // 페이징 처리된 게시글 조회
    Page<PostResponseDto> getPostList(PostType type, Pageable pageable);
    // 사용자별 게시글 조회
    List<PostResponseDto> getPostsByUser(Long userId);

    // 전체 게시글 조회 -> 를 할 일이 있음?
    //List<PostResponseDto> getAllPosts();
    PostResponseDto getPost(Long postId);

    List<AnswerSummaryResponseDto> getAnswersByQuestionId(Long questionId);
    public Post validatePost(Long postId);
}
