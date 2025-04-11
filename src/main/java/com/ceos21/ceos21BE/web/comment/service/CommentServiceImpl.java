package com.ceos21.ceos21BE.web.comment.service;

import com.ceos21.ceos21BE.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.comment.converter.CommentConverter;
import com.ceos21.ceos21BE.web.comment.dto.request.CommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.request.DeleteCommentRequest;
import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponse;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.comment.handler.CommentHandler;
import com.ceos21.ceos21BE.web.comment.repository.CommentRepository;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{


    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest request) {

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CommentHandler(ErrorStatus._USER_NOT_FOUND));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new CommentHandler(ErrorStatus._POST_NOT_FOUND));


        Comment comment = commentConverter.toCommentEntity(request.getContent(), user, post);

        commentRepository.save(comment);

        return commentConverter.toCommentResponse(comment);
    }

    @Override
    @Transactional
    public Void deleteComment(DeleteCommentRequest request) {

        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CommentHandler(ErrorStatus._COMMENT_NOT_FOUND));

        if (!comment.getUser().getUserId().equals(request.getUserId())) {
            throw new CommentHandler(ErrorStatus._USER_NOT_AUTHORIZED);
        }

        commentRepository.delete(comment);

        return null;
    }

    @Override
    public List<CommentResponse> getCommentByUSerId(Long UserId) {
        UserEntity user = userRepository.findById(UserId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus._USER_NOT_FOUND));

        List<Comment> comments = commentRepository.findAllByUser(user);

        if (comments.isEmpty()) {
            throw new CommentHandler(ErrorStatus._COMMENT_NOT_FOUND);
        }

        return comments.stream()
                .map(commentConverter::toCommentResponse)
                .toList();

    }



}
