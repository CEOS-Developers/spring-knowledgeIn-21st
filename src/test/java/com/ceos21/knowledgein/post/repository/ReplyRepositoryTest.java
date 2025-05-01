package com.ceos21.knowledgein.post.repository;

import com.ceos21.knowledgein.post.domain.HashTag;
import com.ceos21.knowledgein.post.domain.Image;
import com.ceos21.knowledgein.post.domain.Post;
import com.ceos21.knowledgein.post.domain.Reply;
import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
@Slf4j
class ReplyRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @BeforeEach
    void setup() {
        UserEntity testUser = makeTestUser("test-user");
        user = userRepository.save(testUser);
        Post testPost = makeTestPostWithImagesAndHashTags(make2PostImages(), make2HashTags());
        userPost = postRepository.save(testPost);
    }

    private static UserEntity user;
    private static Post userPost;


    @Test
    @DisplayName("Post 조회 : 답변 추가")
    void addReplyToPost() {
        // given
        Reply testReply = Reply.createWithNoParent("test", user, userPost, null);
        Reply savedReply = replyRepository.save(testReply);
        log.info("post reply size : {}", savedReply.getPost().getReplies().size());
        // when
        Post post = postRepository.findById(userPost.getId()).get();


        // then


    }




    private Post makeTestPostWithImagesAndHashTags(List<Image> imageList, List<HashTag> hashTagList) {
        return Post.of("test", "test", false, hashTagList, imageList, user);
    }

    private List<HashTag> make2HashTags() {
        return List.of(
                HashTag.createWithNoPost("test1"),
                HashTag.createWithNoPost("test2")
        );
    }

    private UserEntity makeTestUser(String name) {
        return UserEntity.of("test@naver.com" ,name, "test-nick", "1234");
    }

    private List<Image> make2PostImages() {
        return List.of(
                Image.createWithNoPost("test1", "test1"),
                Image.createWithNoPost("test2", "test2")
        );
    }



}