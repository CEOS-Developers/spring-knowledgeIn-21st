package com.ceos21.ceos21BE.web.answer.repository;


import com.ceos21.ceos21BE.web.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
