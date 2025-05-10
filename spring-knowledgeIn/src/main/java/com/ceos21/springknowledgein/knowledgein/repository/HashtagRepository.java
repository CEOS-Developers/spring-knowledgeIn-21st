package com.ceos21.springknowledgein.knowledgein.repository;

import com.ceos21.springknowledgein.knowledgein.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByTagName(String tagName);
}
