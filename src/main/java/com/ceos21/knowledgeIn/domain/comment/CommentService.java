package com.ceos21.knowledgeIn.domain.comment;

import com.ceos21.knowledgeIn.domain.comment.dto.CommentRequestDTO;
import com.ceos21.knowledgeIn.domain.comment.dto.CommentResponseDTO;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //댓글 생성
    @Transactional
    public CommentResponseDTO createComment(Member member, Post post, CommentRequestDTO.create commentRequestDTO) {
        Comment comment = Comment.builder()
                .member(member)
                .content(commentRequestDTO.getContent())
                .post(post)
                .build();

        commentRepository.save(comment);
        post.setCommentCnt(true);

        return CommentResponseDTO.from(comment);
    }

    //특정 게시글의 댓글 목록 조회
    public Page<CommentResponseDTO> getCommentList(Post post, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByPost(post,pageable);
        return comments.map(CommentResponseDTO::from);
    }

    public void deleteComment(Member member, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment.getMember()!=member){
            throw new GeneralException(Status.NOT_AUTHORIZED);
        }
        commentRepository.delete(comment);
    }
}
