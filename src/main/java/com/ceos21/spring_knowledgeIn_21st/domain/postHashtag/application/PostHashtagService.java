package com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.application;

import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.application.HashtagService;
import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.dao.HashtagRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.hashtag.domain.Hashtag;
import com.ceos21.spring_knowledgeIn_21st.domain.post.application.PostService;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.dao.PostHashtagRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.postHashtag.domain.PostHashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostHashtagService {
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagRepository hashtagRepository;

    public void saveHashtag(Post post, List<String>hashtags) {
        for (String content : hashtags) {
            Hashtag hashtag = hashtagRepository.findByContent(content)
                    .orElseGet(()-> hashtagRepository.save(
                            Hashtag.builder()
                                    .content(content)
                                    .postCount(0)
                                    .build()
                    ));
            hashtag.increasePostCount();
            PostHashtag postHashtag = PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();

            post.addPostHashtag(postHashtag);
            postHashtagRepository.save(postHashtag);
        }
    }
}
