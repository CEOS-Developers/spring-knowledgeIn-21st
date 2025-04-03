package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.post.id = :postId")
    List<Comment> findAllByPostId(@Param("postId") Long postId);
}
