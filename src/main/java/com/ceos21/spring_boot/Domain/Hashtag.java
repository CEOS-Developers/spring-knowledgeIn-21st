package com.ceos21.spring_boot.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hashtag")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id", nullable = false, unique = true)
    private Long hashtagId;

    @Column(name = "hashtag_name", length = 50, nullable = false)
    private String hashtagName;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtags;
}
