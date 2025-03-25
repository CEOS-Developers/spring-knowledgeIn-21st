package com.ceos21.ceos21BE.repository;

import com.ceos21.ceos21BE.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
