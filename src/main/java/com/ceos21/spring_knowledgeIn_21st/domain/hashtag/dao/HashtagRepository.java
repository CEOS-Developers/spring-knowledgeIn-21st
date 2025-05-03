package com.ceos21.spring_knowledgeIn_21st.domain.hashtag.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.domain.Hashtag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByContent(@NotBlank String content);
}
