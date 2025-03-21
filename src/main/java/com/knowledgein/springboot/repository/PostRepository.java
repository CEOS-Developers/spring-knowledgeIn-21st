package com.knowledgein.springboot.repository;

import com.knowledgein.springboot.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
