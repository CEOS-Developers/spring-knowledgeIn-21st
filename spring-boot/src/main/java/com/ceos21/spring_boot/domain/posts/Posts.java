package com.ceos21.spring_boot.domain.posts;

import com.ceos21.spring_boot.domain.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "Posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private String pictures;

    private String texts;

    private String tags;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
