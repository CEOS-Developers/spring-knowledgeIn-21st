package com.knowledgein.springboot.converter;

import com.knowledgein.springboot.domain.Hashtag;

public class HashtagConverter {
    public static Hashtag toHashtag(String tag) {
        return Hashtag.builder()
                .tag(tag)
                .build();
    }
}
