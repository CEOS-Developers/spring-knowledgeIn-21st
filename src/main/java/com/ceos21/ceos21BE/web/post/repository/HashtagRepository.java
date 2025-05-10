package com.ceos21.ceos21BE.web.post.repository;

import com.ceos21.ceos21BE.web.post.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
}
