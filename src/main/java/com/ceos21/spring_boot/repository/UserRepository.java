package com.ceos21.spring_boot.repository;

import com.ceos21.spring_boot.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
