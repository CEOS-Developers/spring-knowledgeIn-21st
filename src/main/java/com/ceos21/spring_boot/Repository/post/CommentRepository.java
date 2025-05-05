package com.ceos21.spring_boot.Repository.post;

import com.ceos21.spring_boot.Domain.post.Answer;
import com.ceos21.spring_boot.Domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAnswer(Answer answer); // 특정 답변의 댓글 조회
}

