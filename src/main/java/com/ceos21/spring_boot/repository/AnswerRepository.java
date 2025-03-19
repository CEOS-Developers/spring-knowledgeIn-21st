package com.ceos21.spring_boot.repository;

import com.ceos21.spring_boot.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
