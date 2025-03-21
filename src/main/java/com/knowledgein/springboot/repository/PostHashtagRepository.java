package com.knowledgein.springboot.repository;

import com.knowledgein.springboot.domain.mapping.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
}
