package com.ceos21.spring_knowledgeIn_21st.domain.reaction.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
