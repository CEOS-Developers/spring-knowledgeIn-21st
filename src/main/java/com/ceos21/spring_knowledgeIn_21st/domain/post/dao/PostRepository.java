package com.ceos21.spring_knowledgeIn_21st.domain.post.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
