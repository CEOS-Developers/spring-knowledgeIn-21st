package com.ceos21.ceos21BE.web.comment.service;

import com.ceos21.ceos21BE.global.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.comment.converter.CommentConverter;
import com.ceos21.ceos21BE.web.comment.dto.response.CommentResponseDTO;
import com.ceos21.ceos21BE.web.comment.entity.Comment;
import com.ceos21.ceos21BE.web.comment.handler.CommentHandler;
import com.ceos21.ceos21BE.web.comment.repository.CommentRepository;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.post.service.questionservice.PostService;
import com.ceos21.ceos21BE.web.user.entity.User;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import com.ceos21.ceos21BE.web.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {


    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @Transactional
    public CommentResponseDTO createComment(Long postId, Long userId, String content) {

        User user = userService.validateUser(userId);

        Post post = postService.validatePost(postId);


        Comment comment = commentConverter.toCommentEntity(content, user, post);

        commentRepository.save(comment);

        return commentConverter.toCommentResponse(comment);
    }


    @Transactional
    public void deleteComment(Long commentId, Long userId) {

        Comment comment = validateComment(commentId);

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new CommentHandler(ErrorStatus._USER_NOT_AUTHORIZED);
        }

        commentRepository.delete(comment);

    }


    public List<CommentResponseDTO> getCommentByPostId(Long PostId) {

        Post post = postService.validatePost(PostId);

        List<Comment> comments = commentRepository.findAllByPost(post);

        if (comments.isEmpty()) {
            throw new CommentHandler(ErrorStatus._COMMENT_NOT_FOUND);
        }

        return comments.stream()
                .map(commentConverter::toCommentResponse)
                .toList();

    }

    protected Comment validateComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus._COMMENT_NOT_FOUND));
    }



}
