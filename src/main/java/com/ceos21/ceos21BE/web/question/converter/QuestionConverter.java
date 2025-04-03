package com.ceos21.ceos21BE.web.question.converter;

import com.ceos21.ceos21BE.web.question.dto.request.QuestionRequest;
import com.ceos21.ceos21BE.web.question.dto.response.AllQuestionResponse;
import com.ceos21.ceos21BE.web.question.dto.response.QuestionResponse;
import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionConverter {

    public Question toEntity(QuestionRequest dto, UserEntity user) {
        return Question.create(dto.getTitle(), dto.getContent(), user);
    }

    public QuestionResponse toResponse(Question question) {
        List<String> hashtags = question.getQuestionHashtags().stream()
                .map(questionHashtag -> questionHashtag.getHashtag().getName())
                .toList();

        return QuestionResponse.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .hashtags(hashtags)
                .build();
    }

    public AllQuestionResponse allQuestionResponse(Question question) {
        return AllQuestionResponse.builder()
                .userId(question.getUser().getUserId())
                .username(question.getUser().getUsername())
                .title(question.getTitle())
                .content(question.getContent())
                .hashtags(question.getQuestionHashtags().stream()
                        .map(qh -> qh.getHashtag().getName()).toList())
                .build();
    }
}
