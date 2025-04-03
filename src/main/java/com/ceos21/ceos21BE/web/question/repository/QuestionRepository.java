package com.ceos21.ceos21BE.web.question.repository;

import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByUser(UserEntity user);
}
