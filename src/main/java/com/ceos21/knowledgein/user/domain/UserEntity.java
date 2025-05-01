package com.ceos21.knowledgein.user.domain;

import com.ceos21.knowledgein.global.domain.BaseTimeEntityWithDeletion;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static com.ceos21.knowledgein.user.domain.Role.USER;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "`user`")
@NoArgsConstructor(access = PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE `user` SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserEntity extends BaseTimeEntityWithDeletion {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    // 실명
    @Column(length = 30)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = true)
    private String passWord;

    private boolean isDeleted;
    private boolean isEnabled;

    @Enumerated(STRING)
    private Role role;

    public static UserEntity of(String email, String name, String nickName, String passWord) {
        return UserEntity.builder()
                .name(name)
                .nickName(nickName)
                .passWord(passWord)
                .email(email)
                .role(USER)
                .build();
    }

    @Builder(access = PRIVATE)
    private UserEntity(String name, String email, String nickName, String passWord, Role role) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.passWord = passWord;
    }
}
