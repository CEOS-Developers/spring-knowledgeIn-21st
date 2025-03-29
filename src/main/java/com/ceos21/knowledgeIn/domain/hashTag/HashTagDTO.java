package com.ceos21.knowledgeIn.domain.hashTag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashTagDTO {
    private Long tagId;
    private String tagName;
}
