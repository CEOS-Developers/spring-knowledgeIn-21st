package com.ceos21.knowledgein.repository;

import com.ceos21.knowledgein.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
