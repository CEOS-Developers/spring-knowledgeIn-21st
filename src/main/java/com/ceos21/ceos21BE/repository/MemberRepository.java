package com.ceos21.ceos21BE.repository;

import com.ceos21.ceos21BE.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
