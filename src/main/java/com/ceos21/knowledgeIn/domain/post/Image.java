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

    public static Image create(Post post, String imageUrl) {
        Image image = Image.builder()
                .post(post)
                .imageUrl(imageUrl)
                .build();
        post.addPostImage(image);

        return image;
    }

}
