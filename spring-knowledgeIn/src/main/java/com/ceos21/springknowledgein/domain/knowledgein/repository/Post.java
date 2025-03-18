package com.ceos21.springknowledgein.domain.knowledgein.repository;

import com.ceos21.springknowledgein.domain.user.repository.Member;
import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO_INCREMENT
    private Long id;

    private String title;
    private String content;

    @ManyToOne //Post => Member
    @JoinColumn
    private Member member;

    public Post() {}

    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

}
