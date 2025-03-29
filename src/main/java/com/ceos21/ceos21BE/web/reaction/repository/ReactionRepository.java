package com.ceos21.ceos21BE.web.reaction.repository;

import com.ceos21.ceos21BE.web.reaction.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
