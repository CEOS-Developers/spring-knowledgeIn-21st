package com.ceos21.ceos21BE.web.post.service.questionservice;

import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.DeletePostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.PostRequest;
import com.ceos21.ceos21BE.web.post.dto.request.UpdatePostRequest;
import com.ceos21.ceos21BE.web.post.dto.response.PostResponse;
import com.ceos21.ceos21BE.web.post.entity.PostType;

import java.util.List;


public interface PostService {
    // 게시글 생성 (질문 or 답변)
    PostResponse createPost(CreatePostRequest request, Long userId);

    // 게시글 수정
    PostResponse updatePost(UpdatePostRequest request, Long requestUserId, Long postId);

    // 게시글 삭제
    void deletePost(DeletePostRequest request);

    // 해시태그로 게시글 목록 조회
    List<PostResponse> getPostsByHashtag(String HashtagName);

    // 사용자별 게시글 조회
    List<PostResponse> getPostsByUser(Long userId);

    // 전체 게시글 조회
    List<PostResponse> getAllPosts();
}
