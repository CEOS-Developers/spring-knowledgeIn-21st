package com.ceos21.ceos21BE.web.user.repository;

import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
