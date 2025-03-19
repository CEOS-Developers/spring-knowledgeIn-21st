package com.ceos21.springknowledgein.domain.knowledgein.repository;

import com.ceos21.springknowledgein.domain.user.repository.Member;

import jakarta.persistence.*;

import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter //Getter를 쓰면 getImages() 등의 메소드를 따로 작성 안해도 됨.
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO_INCREMENT
    private Long id;

    private String title;//@Column은 매핑할 때 특별히 이름을 설정할 게 있다면 그때 써도 됨
    private String content;

    @ManyToOne //Post => Member
    @JoinColumn
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")//image 테이블에서 post_id 외래키 추가
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Like> likes = new ArrayList<>();


    public Post() {}

    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void removeImage(Image image) {
        images.remove(image);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }
}
