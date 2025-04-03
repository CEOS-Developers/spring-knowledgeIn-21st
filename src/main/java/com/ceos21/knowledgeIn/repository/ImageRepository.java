package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.post.id = :postId")
    List<Image> findAllByPostId(@Param("postId") Long postId);
}
