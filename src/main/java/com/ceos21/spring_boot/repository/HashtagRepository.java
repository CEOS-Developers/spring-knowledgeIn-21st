package com.ceos21.spring_boot.repository;

import com.ceos21.spring_boot.domain.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
