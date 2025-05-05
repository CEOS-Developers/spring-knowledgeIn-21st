package com.ceos21.spring_boot.Repository.user;

import com.ceos21.spring_boot.Domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Boolean existsByUsername(String username);

    User findByUsername(String username);
}

