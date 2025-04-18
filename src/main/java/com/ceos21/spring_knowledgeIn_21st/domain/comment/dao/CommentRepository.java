package com.ceos21.spring_knowledgeIn_21st.domain.comment.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
