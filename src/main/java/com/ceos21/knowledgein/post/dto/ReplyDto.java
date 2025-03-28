package com.ceos21.knowledgein.post.dto;

import com.ceos21.knowledgein.post.domain.Reply;

import java.util.List;

public record ReplyDto(
        String content,
        boolean accepted,
        List<ImageDto> images,
        List<ReplyChildDto> replyChildren
) {

    public static List<ReplyDto> from(List<Reply> replies) {
        return replies.stream()
                .filter(reply -> reply.getParent() == null)
                .map(ReplyDto::from)
                .toList();
    }

    public static ReplyDto from(Reply reply) {
        return new ReplyDto(
                reply.getContent(),
                reply.isAccepted(),
                ImageDto.from(reply.getImages()),
                ReplyChildDto.from(reply.getReplyChildren())
        );
    }

}
