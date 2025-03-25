package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
