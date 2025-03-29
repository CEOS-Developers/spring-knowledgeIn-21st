package com.ceos21.spring_boot.Repository;

import com.ceos21.spring_boot.Domain.Answer;
import com.ceos21.spring_boot.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByPost(Post post); // 특정 게시글의 답변 조회
}
