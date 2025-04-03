package com.ceos21.knowledgeIn.domain.postHashTag;

import com.ceos21.knowledgeIn.domain.hashTag.HashTag;
import com.ceos21.knowledgeIn.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    Optional<PostHashTag> findByPostAndHashTag(Post post, HashTag hashTag);
}
