package com.ceos21.knowledgein.post.dto;

import com.ceos21.knowledgein.post.domain.HashTag;

import java.util.List;

public record HashTagDto(
    String tag
) {

    public static List<HashTagDto> from(List<HashTag> hashTags) {
        return hashTags.stream()
            .map(HashTagDto::from)
            .toList();
    }

    public static HashTagDto from(HashTag hashTag) {
        return new HashTagDto(
                hashTag.getTag()
        );
    }

}
