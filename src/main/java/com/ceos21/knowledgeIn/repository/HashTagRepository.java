package com.ceos21.knowledgeIn.repository;

import com.ceos21.knowledgeIn.domain.post.HashTag;
import com.ceos21.knowledgeIn.domain.post.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
/*
    @Query("select ph from PostHashTag ph where ph.hashTag.id = :hashTagId")
    List<PostHashTag> findPostHashTagByHashTagId(@Param("hashTagId") Long hashTagId);
*/

    @Modifying
    @Query("delete from PostHashTag ph where ph.hashTag.id = :hashTagId")
    void deletePostHashTagByHashTagId(@Param("hashTagId") Long hashTagId);

    @Query("select h from HashTag h where h.content = :content")
    Optional<HashTag> findByContent(@Param("content") String content);
}
