package com.knowledgein.springboot.repository;

import com.knowledgein.springboot.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
