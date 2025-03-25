package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
