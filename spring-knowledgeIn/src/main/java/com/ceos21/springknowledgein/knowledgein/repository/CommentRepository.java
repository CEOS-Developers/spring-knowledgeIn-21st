package com.ceos21.springknowledgein.knowledgein.repository;

import com.ceos21.springknowledgein.knowledgein.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}