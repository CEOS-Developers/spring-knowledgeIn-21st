package com.ceos21.spring_boot.Repository.post;

import com.ceos21.spring_boot.Domain.post.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByPostId(Long postId);
}
