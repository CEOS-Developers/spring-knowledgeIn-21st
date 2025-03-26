package com.ceos21.knowledgein.user.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ceos21.knowledgein.user.domain.Role.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
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

    public static UserEntity of(String name, String nickName, String passWord) {
        return UserEntity.builder()
                .name(name)
                .nickName(nickName)
                .passWord(passWord)
                .role(USER)
                .build();
    }

    @Builder(access = PRIVATE)
    private UserEntity(String name, String nickName, String passWord, Role role) {
        this.role = role;
        this.name = name;
        this.nickName = nickName;
        this.passWord = passWord;
    }
}
