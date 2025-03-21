package com.ceos21.springknowledgein.domain.knowledgein.repository;

import com.ceos21.springknowledgein.domain.user.repository.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "likes") //like는 예약어여서 테이블명을 바꿨습니다.
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Like() {}

    public Like(Member member) {
        this.member = member;
    }

}
