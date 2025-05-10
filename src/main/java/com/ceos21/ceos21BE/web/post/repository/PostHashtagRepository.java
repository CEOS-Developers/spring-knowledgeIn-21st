package com.ceos21.ceos21BE.web.post.repository;

import com.ceos21.ceos21BE.web.post.entity.Hashtag;
import com.ceos21.ceos21BE.web.post.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findAllByHashtag(Hashtag hashtag);
}
