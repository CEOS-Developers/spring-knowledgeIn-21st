package com.ceos21.knowledgeIn.domain.hashTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByName(String tagName);
}
