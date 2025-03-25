package com.ceos21.springknowledgein.domain.user.repository;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;

@Entity// JPA 엔티티임을 선언
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")// DB에서는 SnakeCase
    private Long id;//자바는 CamelCase

    private String username;
    private String password;

    public Member() {} //JPA를 위한 메서드

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    } //개발자를 위한 메서드
}
/*
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255)
);
 */