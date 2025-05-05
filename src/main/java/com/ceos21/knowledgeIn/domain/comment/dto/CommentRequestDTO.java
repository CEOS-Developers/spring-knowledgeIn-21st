package com.ceos21.knowledgeIn.domain.comment.dto;

import lombok.Getter;

public class CommentRequestDTO {

    @Getter
    public static class create{
        private String content;
    }
}
