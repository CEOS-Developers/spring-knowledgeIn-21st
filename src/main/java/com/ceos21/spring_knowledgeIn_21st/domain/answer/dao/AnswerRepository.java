package com.ceos21.spring_knowledgeIn_21st.domain.answer.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByPostId(Long postId);
}
