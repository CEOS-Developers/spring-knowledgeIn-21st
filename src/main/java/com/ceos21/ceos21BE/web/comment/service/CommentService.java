package com.ceos21.ceos21BE.web.comment.service;

import com.ceos21.ceos21BE.web.comment.dto.request.CommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.request.DeleteCommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    public CommentResponse createComment(CommentRequest request);
    public Void deleteComment(DeleteCommentRequest request);
    public List<CommentResponse> getCommentByUSerId(Long UserId);

}
