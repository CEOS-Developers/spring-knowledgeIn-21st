package com.ceos21.springknowledgein.knowledgein.domain;

import com.ceos21.springknowledgein.user.repository.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Hate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Hate() {}

    public Hate(Member member) {
        this.member = member;
    }
}
