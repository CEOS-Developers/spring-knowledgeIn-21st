package com.ceos21.ceos21BE.web.post.repository;

import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUser(UserEntity user);
    List<Post> findByPostType(PostType postType);
    List<Post> findByUserAndPostType(UserEntity user, PostType postType);
    Boolean existsByParent(Post post);
}
