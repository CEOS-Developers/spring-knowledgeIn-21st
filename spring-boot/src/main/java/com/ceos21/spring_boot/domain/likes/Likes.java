package com.ceos21.spring_boot.domain.likes;

import com.ceos21.spring_boot.domain.posts.Posts;
import com.ceos21.spring_boot.domain.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Posts post;

    @Enumerated(EnumType.STRING)
    private TypeOfLikes typeOfLike;

    private LocalDateTime createdAt;
}
