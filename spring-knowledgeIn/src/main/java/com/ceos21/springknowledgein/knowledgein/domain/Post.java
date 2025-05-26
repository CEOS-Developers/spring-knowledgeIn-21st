package com.ceos21.springknowledgein.knowledgein.domain;

import com.ceos21.springknowledgein.user.repository.Member;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter //Getter를 쓰면 getImages() 등의 메소드를 따로 작성 안해도 됨.
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO_INCREMENT
    private Long id;

    private String title;//@Column은 매핑할 때 특별히 이름을 설정할 게 있다면 그때 써도 됨
    private String content;

    @ManyToOne //Post => Member
    @JoinColumn(name = "member_id")
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Hate> hates = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHashtag> postHashtags = new ArrayList<>();

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

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
    }

    public void addHate(Hate hate) {
        hates.add(hate);
    }

    public void removeHate(Hate hate) {
        hates.remove(hate);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addPostHashtag(PostHashtag postHashtag) {
        postHashtags.add(postHashtag);
        postHashtag.setPost(this);
    }


}
