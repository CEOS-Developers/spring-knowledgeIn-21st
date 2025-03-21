package com.knowledgein.springboot.repository;

import com.knowledgein.springboot.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
