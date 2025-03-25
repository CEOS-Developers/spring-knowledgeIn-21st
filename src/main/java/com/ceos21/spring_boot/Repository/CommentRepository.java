package com.ceos21.spring_boot.Repository;

import com.ceos21.spring_boot.Domain.Comment;
import com.ceos21.spring_boot.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post); // 특정 게시글의 댓글 조회
}

