package com.ceos21.spring_knowledgeIn_21st.domain.image.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.image.domain.Image;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByPost(@NotNull Post post);
}
