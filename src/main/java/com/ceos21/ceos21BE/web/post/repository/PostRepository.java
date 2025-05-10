package com.ceos21.ceos21BE.web.post.repository;

import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.entity.PostType;
import com.ceos21.ceos21BE.web.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;


import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUser(User user);
    Boolean existsByParent(Post post);
    Page<Post> findAllByPostType(PostType postType, Pageable pageable);

}
