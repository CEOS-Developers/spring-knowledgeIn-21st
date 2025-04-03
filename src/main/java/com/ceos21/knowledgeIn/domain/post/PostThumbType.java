package com.ceos21.knowledgeIn.domain.post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostThumbType {
    LIKE("like", "좋아요"), DISLIKE("dislike", "싫어요");
    private final String key;
    private final String value;
}
