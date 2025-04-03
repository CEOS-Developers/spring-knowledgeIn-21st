package com.ceos21.ceos21BE.web.comment.repository;

import com.ceos21.ceos21BE.web.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
