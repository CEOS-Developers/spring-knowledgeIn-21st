package com.ceos21.springknowledgein.knowledgein.repository;

import com.ceos21.springknowledgein.knowledgein.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HateRepository extends JpaRepository<Image, Long> {}
