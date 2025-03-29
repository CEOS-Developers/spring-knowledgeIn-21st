package com.ceos21.ceos21BE.config.dummy;

import com.ceos21.ceos21BE.web.hashtag.entity.Hashtag;
import com.ceos21.ceos21BE.web.hashtag.repository.HashtagRepository;
import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.question.repository.QuestionRepository;
import com.ceos21.ceos21BE.web.questionhashtag.entity.QuestionHashtag;
import com.ceos21.ceos21BE.web.questionhashtag.repository.QuestionHashtagRepository;
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
    private final QuestionRepository questionRepository;
    private final HashtagRepository hashtagRepository;
    private final QuestionHashtagRepository questionHashtagRepository;

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
            Question question = Question.builder()
                    .title("테스트용 질문입니다")
                    .content("이건 더미 데이터예요.")
                    .user(user)
                    .questionHashtags(new ArrayList<>()) // 초기화 중요!
                    .build();

            questionRepository.save(question);

            // 해시태그 추가
            List<String> tags = List.of("더미", "테스트");
            for (String tagName : tags) {
                Hashtag tag = hashtagRepository.findByName(tagName)
                        .orElseGet(() -> hashtagRepository.save(
                                Hashtag.builder().name(tagName).build()
                        ));

                QuestionHashtag qh = QuestionHashtag.builder()
                        .question(question)
                        .hashtag(tag)
                        .build();

                question.getQuestionHashtags().add(qh);
                questionHashtagRepository.save(qh);
            }
        }
    }
}
