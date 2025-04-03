package com.ceos21.knowledgeIn.domain.post;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id")
    private Post post;

    public static Image create(Post post, String imageUrl) {    //장점: 필수 필드 강제 & 편의 메서드를 놓치지 않음. 단점: 빌더패턴의 자유로운 생성자 구조가 사라짐&객체 생성과 연관관계 설정이 한 곳에서 이루어짐
        if (post == null || imageUrl == null) throw new IllegalArgumentException("인자에 Null 값이 있습니다");

        Image image = Image.builder()
                .post(post)
                .imageUrl(imageUrl)
                .build();
        post.addPostImage(image);

        return image;
    }

    public void updateUrl(String newFileUrl) {
        this.imageUrl = newFileUrl;
    }
}
