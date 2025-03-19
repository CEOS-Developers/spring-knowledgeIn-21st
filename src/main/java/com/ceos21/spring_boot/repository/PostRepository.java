package com.ceos21.spring_boot.repository;

import com.ceos21.spring_boot.domain.entity.Post;
import com.ceos21.spring_boot.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByWriter(User writer);
}
