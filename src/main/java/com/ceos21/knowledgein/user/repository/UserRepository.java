package com.ceos21.knowledgein.user.repository;

import com.ceos21.knowledgein.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
