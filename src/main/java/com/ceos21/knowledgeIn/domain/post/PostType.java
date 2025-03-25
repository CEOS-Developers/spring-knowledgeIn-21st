package com.ceos21.knowledgeIn.domain.post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostType {
       QUESTION("post_question", "질문글"), ANSWER("post_answer","답변글");

       private final String key;
       private final String value;
}
