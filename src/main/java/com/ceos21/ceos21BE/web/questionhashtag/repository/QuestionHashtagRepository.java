package com.ceos21.ceos21BE.web.questionhashtag.repository;

import com.ceos21.ceos21BE.web.hashtag.entity.Hashtag;
import com.ceos21.ceos21BE.web.questionhashtag.entity.QuestionHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionHashtagRepository extends JpaRepository<QuestionHashtag, Long> {
    List<QuestionHashtag> findAllByHashtag(Hashtag hashtag);
}
