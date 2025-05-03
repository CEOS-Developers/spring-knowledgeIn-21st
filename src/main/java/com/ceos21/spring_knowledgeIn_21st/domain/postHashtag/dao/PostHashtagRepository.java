package com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.domain.Hashtag;
import com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findByHashtag(Hashtag hashtag);
}