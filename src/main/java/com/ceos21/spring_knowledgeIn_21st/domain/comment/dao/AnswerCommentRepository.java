package com.ceos21.spring_knowledgeIn_21st.domain.comment.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import com.ceos21.spring_knowledgeIn_21st.domain.comment.domain.AnswerComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerCommentRepository extends JpaRepository<AnswerComment, Long> {
    List<AnswerComment> findByAnswerId(Long answerId);
    
    @Query("SELECT ac FROM AnswerComment ac JOIN FETCH ac.user JOIN FETCH ac.answer WHERE ac.id = :id")
    Optional<AnswerComment> findByIdWithUserAndAnswer(@Param("id") Long id);
}
