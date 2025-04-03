package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.Hashtag;
import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.domain.mapping.PostHashtag;

public class PostHashtagConverter {
    public static PostHashtag toPostHashtag(Post post, Hashtag hashtag) {
        return PostHashtag.builder()
                .hashtag(hashtag)
                .post(post)
                .build();
    }
}
