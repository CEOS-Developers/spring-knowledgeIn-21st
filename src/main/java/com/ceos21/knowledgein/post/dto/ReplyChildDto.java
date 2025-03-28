package com.ceos21.knowledgein.post.dto;

import com.ceos21.knowledgein.post.domain.Reply;

import java.util.List;

public record ReplyChildDto(
        String content
) {

    public static List<ReplyChildDto> from(List<Reply> replies) {
        return replies.stream()
                .map(ReplyChildDto::from)
                .toList();
    }

    public static ReplyChildDto from(Reply reply) {
        return new ReplyChildDto(
                reply.getContent()
        );
    }
}
