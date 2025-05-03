package com.ceos21.spring_knowledgeIn_21st.domain.comment.application;

import com.ceos21.spring_knowledgeIn_21st.domain.comment.dao.PostCommentRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.BaseComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.PostComment;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.request.CommentAddRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.dto.request.CommentUpdateRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dao.PostRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.user.dao.UserRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import com.ceos21.spring_knowledgeIn_21st.global.exception.CustomException;
import com.ceos21.spring_knowledgeIn_21st.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;


    /**
     * 게시글의 댓글 전체 조회
     * @param postId
     * @return
     */
    public List<PostComment> findAllComments(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return postCommentRepository.findByPostId(postId);
    }

    /**
     * 게시글 댓글 추가
     * @param request
     * @param userId
     * @return
     */
    @Transactional
    public PostComment addComment(CommentAddRequest request, Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        PostComment comment = request.toPostCommentEntity(post, user);
        post.addPostComment(comment);
        return postCommentRepository.save(comment);
    }


    /**
     * 댓글 삭제
     * @param postId
     * @param commentId
     * @param userId
     */
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }
        postCommentRepository.delete(comment);
    }

    /**
     * 댓글 수정
     * @param request
     * @param postId
     * @param commentId
     * @param userId
     * @return
     */
    @Transactional
    public PostComment updateComment(CommentUpdateRequest request, Long postId, Long commentId, Long userId) {
        PostComment comment = postCommentRepository.findByIdWithUserAndPost(commentId)
                .orElseThrow(()->new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }
        if (!comment.getPost().getId().equals(postId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        comment.update(request.content());
        return comment;
    }
}
