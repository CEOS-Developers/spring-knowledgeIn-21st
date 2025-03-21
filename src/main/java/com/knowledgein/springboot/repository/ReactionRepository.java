package com.knowledgein.springboot.repository;

import com.knowledgein.springboot.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
