package com.ceos21.knowledgeIn.service;

import com.ceos21.knowledgeIn.controller.dto.post.CommentCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.CommentResponseDTO;
import com.ceos21.knowledgeIn.domain.post.Comment;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.CommentRepository;
import com.ceos21.knowledgeIn.repository.PostRepository;
import com.ceos21.knowledgeIn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    //create
    @Transactional

    public Long createComment(CommentCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();

        Comment entity = Comment.create(user, post, dto.getContent());
        return commentRepository.save(entity).getId();
    }

    //read
    public CommentResponseDTO findComment(Long id) {
        Comment findComment = commentRepository.findById(id).orElseThrow();

        return CommentResponseDTO.from(findComment);
    }

    public List<CommentResponseDTO> findAllComments() {
        return commentRepository.findAll().stream()
                .map(comment -> CommentResponseDTO.from(comment))
                .collect(Collectors.toList());
    }

    //update
    //delete
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<CommentResponseDTO> findAllCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        return comments.stream()
                .map(comment -> CommentResponseDTO.from(comment))
                .collect(Collectors.toList());
    }
}
