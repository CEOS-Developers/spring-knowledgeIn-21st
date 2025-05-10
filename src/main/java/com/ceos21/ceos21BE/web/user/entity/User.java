package com.ceos21.ceos21BE.web.user.entity;

import com.ceos21.ceos21BE.global.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "`user`")
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 현재 실명이 아닌 닉네임으로 관리
    @NotBlank
    @Column(length = 30)
    private String username;

    @NotBlank @Email
    @Column(nullable = true, unique = true)
    private String email;

    @NotBlank
    private String password;

    // role 이 필요한 이유는 모르겠다.
    //private String role;

    // User은 단방향으로 관리하겠다.
    public void updateUsername(String username) {
        this.username = username;
    }
}
