package com.ceos21.spring_boot.domain.tags.dto;

import com.ceos21.spring_boot.domain.tags.Tags;
import lombok.Getter;

@Getter
public class TagsResponse {

    private Long id;
    private String tag;
    private Long postId;

    public static TagsResponse from(Tags tags) {
        TagsResponse response = new TagsResponse();
        response.id = tags.getId();
        response.tag = tags.getTag();
        response.postId = tags.getPost().getId();
        return response;
    }
}
