package com.ceos21.ceos21BE.config.dummy;

import com.ceos21.ceos21BE.web.hashtag.entity.Hashtag;
import com.ceos21.ceos21BE.web.hashtag.repository.HashtagRepository;
import com.ceos21.ceos21BE.web.post.entity.Post;
import com.ceos21.ceos21BE.web.post.repository.PostRepository;
import com.ceos21.ceos21BE.web.posthashtag.entity.PostHashtag;
import com.ceos21.ceos21BE.web.posthashtag.repository.PostHashtagRepository;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Dummy implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            UserEntity user = userRepository.save(UserEntity.builder()
                    .username("tester")
                    .email("tester@example.com")
                    .password("1234")
                    .build());

            UserEntity user2 = userRepository.save(UserEntity.builder()
                    .username("한혜수")
                    .email("heasoo@google.com")
                    .password("0101")
                    .build());

            // 질문글 생성
            Post post = Post.builder()
                    .title("테스트용 질문입니다")
                    .content("이건 더미 데이터예요.")
                    .user(user)
                    .postHashtags(new ArrayList<>()) // 초기화 중요!
                    .build();

            postRepository.save(post);

            // 해시태그 추가
            List<String> tags = List.of("더미", "테스트");
            for (String tagName : tags) {
                Hashtag tag = hashtagRepository.findByName(tagName)
                        .orElseGet(() -> hashtagRepository.save(
                                Hashtag.builder().name(tagName).build()
                        ));

                PostHashtag qh = PostHashtag.builder()
                        .post(post)
                        .hashtag(tag)
                        .build();

                post.getPostHashtags().add(qh);
                postHashtagRepository.save(qh);
            }
        }
    }
}
