package com.ceos21.knowledgein.user.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "`user`")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class UserEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String passWord;

    @Enumerated(STRING)
    private Role role;

    @Builder
    public UserEntity(String name, String nickName, String passWord, Role role) {
        this.name = name;
        this.nickName = nickName;
        this.passWord = passWord;
        this.role = role;
    }
}
