package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

//좋아요/싫어요 구분이 불가능함 개선필요
//SoftDelete 기능 추가? : 개선사항
//반복적인 좋아요/좋아요취소 작업으로 인한 오버헤드 감소(상태만 변경하므로 delete,insert작업 또는 객체 생성 삭제이 필요가 없음)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class PostThumb {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postthumb_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PostThumbType postThumbType;

    public static PostThumb createLike(Post post, User user) {
        if(post == null || user == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");

        PostThumb postThumb = PostThumb.builder()
                .post(post)
                .user(user)
                .postThumbType(PostThumbType.LIKE)
                .build();

        post.addPostThumb(postThumb);

        return postThumb;
    }

    public static PostThumb createDisLike(Post post, User user) {
        if (post == null || user == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");

        PostThumb postThumb = PostThumb.builder()
                .post(post)
                .user(user)
                .postThumbType(PostThumbType.DISLIKE)
                .build();

        post.addPostThumb(postThumb);

        return postThumb;
    }
}
