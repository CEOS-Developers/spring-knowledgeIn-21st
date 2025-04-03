package com.ceos21.spring_boot.Repository;

import com.ceos21.spring_boot.Domain.Answer;
import com.ceos21.spring_boot.Domain.Reaction;
import com.ceos21.spring_boot.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByAnswerAndUser(Answer answer, User user);
}
