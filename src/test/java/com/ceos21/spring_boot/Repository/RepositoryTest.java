/*package com.ceos21.spring_boot.Repository;

import com.ceos21.spring_boot.Domain.Comment;
import com.ceos21.spring_boot.Domain.Post;
import com.ceos21.spring_boot.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 연결을 원할 경우
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private User createUser(String id,String password, String email, String username, String birthdate, String gender, String phonenumber) {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(username);
        user.setBirthdate(birthdate);
        user.setGender(gender);
        user.setPhoneNumber(phonenumber);
        return userRepository.save(user); // DB에 저장
    }

    private Post createPost(String title, String content, User user) {
        Post post = new Post();
        post.setTitle(title);
        post.setPostContent(content);
        post.setUser(user);
        return post;
    }

    private Comment createComment(String text, Post post, User user) {
        Comment comment = new Comment();
        comment.setCommentContent(text);
        comment.setPost(post);
        comment.setUser(user);
        comment.setLikeCount(0);
        comment.setDislikeCount(0);
        comment.setParentComment(null);  // 부모 댓글이 없으면 null로 설정
        comment.setChildComments(null);  // 자식 댓글이 없으면 null로 설정
        return comment;
    }

    @org.junit.jupiter.api.Test
    public void testPostAndCommentRelationship() {
        // given
        User user1 = createUser("User1","1111","user1@gmail.com","User1!","010101","여","010-1111-1111");
        User user2 = createUser("User2","2222","user2@gmail.com","User2!","020202","남","010-2222-2222");

        Post post1 = createPost("Post 1", "Content for Post 1", user1);
        Post post2 = createPost("Post 2", "Content for Post 2", user2);
        Post post3 = createPost("Post 3", "Content for Post 3", user1);

        postRepository.saveAll(Arrays.asList(post1, post2, post3));

        Comment comment1 = createComment("Comment 1", post1, user1);
        Comment comment2 = createComment("Comment 2", post1, user2);
        Comment comment3 = createComment("Comment 3", post2, user1);
        Comment comment4 = createComment("Comment 4", post3, user2);

        commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3, comment4));

        // when
        List<Comment> commentsForPost1 = commentRepository.findByPost(post1);
        List<Comment> commentsForPost2 = commentRepository.findByPost(post2);
        List<Comment> commentsForPost3 = commentRepository.findByPost(post3);

        // then
        assertEquals(2, commentsForPost1.size());
            // Post 1에 달린 댓글 목록(commentsForPost1)에서 특정 댓글이 존재하는지 검증
        assertTrue(commentsForPost1.stream().anyMatch(c -> c.getCommentContent().equals("Comment 1")));
        assertTrue(commentsForPost1.stream().anyMatch(c -> c.getCommentContent().equals("Comment 2")));

        assertEquals(1, commentsForPost2.size());
        assertTrue(commentsForPost2.stream().anyMatch(c -> c.getCommentContent().equals("Comment 3")));

        assertEquals(1, commentsForPost3.size());
        assertTrue(commentsForPost3.stream().anyMatch(c -> c.getCommentContent().equals("Comment 4")));
    }


}
 */
