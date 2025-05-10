package com.ceos21.springknowledgein.knowledgein.repository;

import com.ceos21.springknowledgein.knowledgein.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}