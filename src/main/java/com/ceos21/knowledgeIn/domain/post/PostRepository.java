package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.common.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByParent(Post post, Pageable pageable);

    Page<Post> findByContentContains(String search, Pageable pageable);

    Page<Post> findByContentContainsAndPostType(String search, Pageable pageable, PostType postType);
}
