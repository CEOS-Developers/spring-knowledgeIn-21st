package com.ceos21.spring_boot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    // DTO 변환 메서드는 repository에 두지 않고, service에서 처리하도록 수정
}
