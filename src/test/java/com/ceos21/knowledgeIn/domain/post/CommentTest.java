package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.CommentRepository;
import com.ceos21.knowledgeIn.repository.PostRepository;
import com.ceos21.knowledgeIn.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class CommentTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void createComment() {
        //given
        Post post = Post.createQuestion("title", "content");
        postRepository.save(post);

        User user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        Comment comment = Comment.create(user, post, post.getContent());

        //when
        Long id = commentRepository.save(comment).getId();
        //then
        Optional<Comment> findComment = commentRepository.findById(id);
        assertThat(findComment).isPresent();
        assertThat(findComment.get().getPost()).isEqualTo(post);
        assertThat(findComment.get().getUser()).isEqualTo(user);
    }
    @Test
    void deleteComment() {
        //given
        Post post = Post.createQuestion("title", "content");
        postRepository.save(post);

        User user = User.builder()
                .name("최근호")
                .nickname("olaf")
                .build();
        userRepository.save(user);

        Comment comment = Comment.create(user, post, post.getContent());

        //when
        Long id = commentRepository.save(comment).getId();
        commentRepository.deleteById(id);
        //then
        Optional<Comment> findComment = commentRepository.findById(id);
        assertThat(findComment).isEmpty();
    }
}