package com.knowledgein.springboot.domain;

import com.knowledgein.springboot.domain.mapping.PostHashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 10, unique = true)
    private String tag;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostHashtag> postHashtagList = new ArrayList<>();

    public void addPostHashtag(PostHashtag postHashtag) {
        postHashtagList.add(postHashtag);
    }
}
