package com.ceos21.knowledgein.post.dto.request;

import jakarta.validation.constraints.NotNull;

public record RequestCreateReplyChildren(
        @NotNull
        String content
) {
}
