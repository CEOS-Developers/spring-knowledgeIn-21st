package com.ceos21.spring_knowledgeIn_21st.domain.reaction.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.Reaction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("SELECT r FROM Reaction r JOIN FETCH r.answer WHERE r.user.id = :userId AND r.answer.id = :answerId")
    Optional<Reaction> findByUserIdAndAnswerId(@Param("userId") Long userId, @Param("answerId") Long answerId);
}
