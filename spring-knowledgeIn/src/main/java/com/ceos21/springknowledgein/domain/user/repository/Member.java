package com.ceos21.springknowledgein.domain.user.repository;

import com.ceos21.springknowledgein.domain.user.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;



@Entity// JPA 엔티티임을 선언
@Getter
@NoArgsConstructor
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//자바는 CamelCase

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;


    public Member(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
