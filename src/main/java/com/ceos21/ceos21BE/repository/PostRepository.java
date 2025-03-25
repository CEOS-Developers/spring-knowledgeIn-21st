package com.ceos21.ceos21BE.repository;

import com.ceos21.ceos21BE.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);
}
