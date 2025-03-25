package com.ceos21.knowledgeIn.domain.post;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//셀프 참조를 하므로 무한루프 주의필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_Id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    //질문(부모) 게시글을 참조
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_post_id")
    private Post parentPost;

    //답변(자식) 게시글을 참조
    @OneToMany(mappedBy = "parentPost") //CASCADE 필요?
    private List<Post> childPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true) //Post가  PostThumb의 영속성을 관리
    private List<PostHashTag> hashTags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true)  //Post가  PostThumb의 영속성을 관리
    private List<PostThumb> postThumbs = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Post(
            String title,
            String content,
            PostType postType,
            Post parentPost
    ) {
        this.title = title;
        this.content = content;
        this.postType = postType;

        // 질문글 VS 답변글 생성 로직
        if (this.postType == PostType.ANSWER) { //답변글
            if (parentPost == null) {
                throw new IllegalArgumentException("답변글(ANSWER)은 parentPost가 필수입니다.");
            }
            this.parentPost = parentPost;
        } else {                                //질문글
            this.parentPost = null;
        }


    }

    // (옵션) postType과 parentPost를 받는 정적 팩터리 메서드
    // 굳이 써야 하는 건 아니지만, 외부에서 조금 더 읽기 편할 수 있음
    public static Post createQuestion(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .postType(PostType.QUESTION)
                .build();
    }

    public static Post createAnswer(String title, String content, Post parentPost) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .postType(PostType.ANSWER)
                .parentPost(parentPost)
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
}
