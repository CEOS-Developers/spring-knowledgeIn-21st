package com.ceos21.spring_boot.repository;

import com.ceos21.spring_boot.domain.entity.LikeDislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {
}
