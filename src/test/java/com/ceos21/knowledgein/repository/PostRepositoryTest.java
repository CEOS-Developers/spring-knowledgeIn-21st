package com.ceos21.knowledgein.repository;

import com.ceos21.knowledgein.domain.HashTag;
import com.ceos21.knowledgein.domain.Post;
import com.ceos21.knowledgein.domain.PostImage;
import com.ceos21.knowledgein.domain.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static com.ceos21.knowledgein.domain.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostImageRepository postImageRepository;

    private static UserEntity user;
    private static Post userPost;

    @BeforeEach
    void setup() {
        UserEntity testUser = makeTestUser("test-user");
        user = userRepository.save(testUser);
        Post testPost = makeTestPostWithImagesAndHashTags(make2PostImages(), make2HashTags());
        userPost = postRepository.save(testPost);
    }

    @Test
    @DisplayName("연관관계 없이 Post save 테스트")
    void savePostTestWithNoRelation() {
        // given
        Post newPost = makeTestPost("test", "test");
        // when
        Post savedPost = postRepository.save(newPost);
        // then
        assertThat(newPost).isEqualTo(savedPost);
        assertThat(newPost.getUser()).isEqualTo(savedPost.getUser());
    }

    @Test
    @DisplayName("Cascade 작동 확인: 연관관계가 있는 Post save")
    @Transactional
    void savePostTestWithRelation() {
        // given
        List<PostImage> postImageList = make2PostImages();
        Post newPost = makeTestPostWithImages(postImageList);
        // when
        Post savedPost = postRepository.save(newPost);
        // then
        assertThat(newPost).isEqualTo(savedPost);
        assertThat(savedPost.getPostImages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Cascade 작동 확인: 연관관계가 있는 Post save - 해시태그와 이미지 모두")
    @Transactional
    void savePostTestWithRelationHashTagAndImages() {
        // given
        List<PostImage> postImageList = make2PostImages();
        List<HashTag> hashTagList = make2HashTags();
        Post newPost = makeTestPostWithImagesAndHashTags(postImageList, hashTagList);
        // when
        Post savedPost = postRepository.save(newPost);
        // then
        assertThat(newPost).isEqualTo(savedPost);
        assertThat(savedPost.getPostImages().size()).isEqualTo(2);
        assertThat(savedPost.getHashTags().size()).isEqualTo(2);
    }


    @Test
    @DisplayName("Cascade 작동 확인: Post 삭제시 PostImage도 삭제")
    @Transactional
    void deletePostAndCascadeDeleteImages() {
        // given
        postRepository.delete(userPost);
        // when
        Optional<Post> findPost = postRepository.findById(userPost.getId());
        // then
        assertThat(findPost).isEmpty();
        userPost.getPostImages().forEach(postImage -> {
            Optional<PostImage> findPostImage = postImageRepository.findById(postImage.getId());
            assertThat(findPostImage).isEmpty();
        });
    }

    @Test
    @DisplayName("Post 조회 : 제목으로 조회")
    void viewPost() {
        // given
        // when
        List<Post> findPostList = postRepository.findByTitleContaining("test");
        // then
        assertThat(findPostList.size()).isEqualTo(1);
        assertThat(findPostList.get(0)).isEqualTo(userPost);
    }

    @Test
    @DisplayName("Post 조회 : 정보 출력")
    void viewPostWithImages() {
        // given
        // when
        List<Post> allByUserId = postRepository.findAllByUserId(user.getId());

        log.info("allByUserId : {}", allByUserId);
        log.info("allByUserId.get(0).getPostImages() : {}", allByUserId.get(0).getPostImages());
        log.info("allByUserId.get(0).getHashTags() : {}", allByUserId.get(0).getHashTags());


        // then
        assertThat(allByUserId.size()).isEqualTo(1);
        assertThat(allByUserId.get(0)).isEqualTo(userPost);
        assertThat(allByUserId.get(0).getPostImages().size()).isEqualTo(2);
        assertThat(allByUserId.get(0).getHashTags().size()).isEqualTo(2);

    }


    private Post makeTestPostWithImagesAndHashTags(List<PostImage> postImageList, List<HashTag> hashTagList) {
        return Post.builder()
                .title("test")
                .content("test")
                .nicknamePublic(false)
                .user(user)
                .postImages(postImageList)
                .hashTags(hashTagList)
                .build();
    }

    private List<HashTag> make2HashTags() {
        return List.of(
                HashTag.builder()
                        .tag("test1")
                        .build(),
                HashTag.builder()
                        .tag("test2")
                        .build()
        );
    }

    private UserEntity makeTestUser(String name) {
        return UserEntity.builder()
                .name(name)
                .nickName("test-nick")
                .passWord("1234")
                .role(USER)
                .build();
    }

    private Post makeTestPost(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .nicknamePublic(true)
                .user(user)
                .build();
    }

    private List<PostImage> make2PostImages() {
        return List.of(
                PostImage.builder()
                        .storageUrl("test1")
                        .uploadFileName("test1")
                        .build(),
                PostImage.builder()
                        .storageUrl("test2")
                        .uploadFileName("test2")
                        .build()
        );
    }

    private Post makeTestPostWithImages(List<PostImage> postImageList) {
        return Post.builder()
                .title("test")
                .content("test")
                .nicknamePublic(false)
                .user(user)
                .postImages(postImageList)
                .build();
    }
}