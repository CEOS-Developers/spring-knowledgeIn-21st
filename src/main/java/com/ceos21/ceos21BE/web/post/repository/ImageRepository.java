package com.ceos21.ceos21BE.web.post.repository;

import com.ceos21.ceos21BE.web.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
