package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select ph.post from PostHashTag ph where ph.hashTag.id = :hashTagId")
    List<Post> findPostByHashTagId(@Param("hashTagId") Long hashTagId);

}
