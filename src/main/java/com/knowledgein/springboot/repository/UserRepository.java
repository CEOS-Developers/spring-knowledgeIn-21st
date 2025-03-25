package com.knowledgein.springboot.repository;

import com.knowledgein.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
