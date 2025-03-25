package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
