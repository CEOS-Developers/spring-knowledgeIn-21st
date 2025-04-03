package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.common.BaseTimeEntity;
import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//셀프 참조를 하므로 무한루프 주의필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Post extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_Id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    //질문(부모) 게시글을 참조
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    //답변(자식) 게시글을 참조
    @Builder.Default
    @OneToMany(mappedBy = "parentPost") //CASCADE 필요?
    private List<Post> childPosts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true) //Post가  PostHashTag의 영속성을 관리
    private List<PostHashTag> hashTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true)  //Post가  PostThumb의 영속성을 관리
    private List<PostThumb> postThumbs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    // (옵션) postType과 parentPost를 받는 정적 팩터리 메서드
    // 굳이 써야 하는 건 아니지만, 외부에서 조금 더 읽기 편할 수 있음
    public static Post createQuestion(String title, String content, User writer) {
        if (title == null || content == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");

        return Post.builder()
                .title(title)
                .content(content)
                .postType(PostType.QUESTION)
                .user(writer)
                .build();
    }

    public static Post createAnswer(String title, String content, Post parentPost, User writer) {
        if (title == null || content == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");
        if (parentPost == null) throw new IllegalArgumentException("답변글(ANSWER)은 parentPost가 필수입니다.");
        
        Post post = Post.builder()
                .title(title)
                .content(content)
                .postType(PostType.ANSWER)
                .parentPost(parentPost)
                .user(writer)
                .build();
        parentPost.addChildPost(post);

        return post;
    }


    //연관관계 편의 메서드
    public void addPostHashTag(PostHashTag entity) {
        hashTags.add(entity);
    }

    public void addPostImage(Image image) {
        images.add(image);
    }

    public void addPostThumb(PostThumb entity) {
        postThumbs.add(entity);
    }

    public void addComment(Comment entity) {
        comments.add(entity);
    }

    public void addChildPost(Post childPost) {
        childPosts.add(childPost);
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;

//        newHashTags.forEach(hashtag->hashTags.add(hashtag));
    }

    public void updateImages(List<Image> newImages) {
        this.images.clear();

        newImages.forEach(image -> images.add(image));
    }
}
