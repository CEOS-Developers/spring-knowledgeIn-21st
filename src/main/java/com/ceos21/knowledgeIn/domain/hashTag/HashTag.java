package com.ceos21.knowledgeIn.domain.hashTag;

import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTag;
import com.ceos21.knowledgeIn.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class HashTag extends BaseEntity {

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "hashtag")
    private List<PostHashTag> postHashTags = new ArrayList<>();
}
