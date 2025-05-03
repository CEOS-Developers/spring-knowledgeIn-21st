package com.ceos21.spring_knowledgeIn_21st.domain.hashtag.application;

import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.dao.HashtagRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.domain.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Optional<Hashtag> findByContent(String content) {
        return hashtagRepository.findByContent(content);
    }

    public Hashtag save(Hashtag hashtag) {
        return hashtagRepository.save(hashtag);
    }
}
