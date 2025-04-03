package com.ceos21.knowledgein.post.repository;

import com.ceos21.knowledgein.post.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<Image, Long> {
}
