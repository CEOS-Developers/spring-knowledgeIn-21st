package com.ceos21.spring_boot.repository;

import com.ceos21.spring_boot.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
