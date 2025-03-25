package com.ceos21.springknowledgein.domain.knowledgein.repository;

import com.ceos21.springknowledgein.domain.user.repository.Member;
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
