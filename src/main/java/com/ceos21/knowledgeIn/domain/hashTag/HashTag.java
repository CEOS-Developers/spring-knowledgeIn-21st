package com.ceos21.knowledgeIn.domain.hashTag;

import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTag;
import com.ceos21.knowledgeIn.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTag extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "hashtag")
    private List<PostHashTag> postHashTags;
}
