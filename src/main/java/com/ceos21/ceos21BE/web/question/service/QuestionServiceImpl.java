package com.ceos21.ceos21BE.web.question.service;

import com.ceos21.ceos21BE.apiPayload.code.status.ErrorStatus;
import com.ceos21.ceos21BE.web.hashtag.entity.Hashtag;
import com.ceos21.ceos21BE.web.hashtag.repository.HashtagRepository;
import com.ceos21.ceos21BE.web.question.converter.QuestionConverter;
import com.ceos21.ceos21BE.web.question.dto.request.QuestionRequest;
import com.ceos21.ceos21BE.web.question.dto.response.AllQuestionResponse;
import com.ceos21.ceos21BE.web.question.dto.response.QuestionResponse;
import com.ceos21.ceos21BE.web.question.entity.Question;
import com.ceos21.ceos21BE.web.question.handler.QuestionHandler;
import com.ceos21.ceos21BE.web.question.repository.QuestionRepository;
import com.ceos21.ceos21BE.web.questionhashtag.entity.QuestionHashtag;
import com.ceos21.ceos21BE.web.questionhashtag.repository.QuestionHashtagRepository;
import com.ceos21.ceos21BE.web.user.entity.UserEntity;
import com.ceos21.ceos21BE.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final HashtagRepository hashtagRepository;
    private final QuestionHashtagRepository questionHashtagRepository;
    private final UserRepository userRepository;
    private final QuestionConverter converter;

    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {

        if(request.getTitle() == null) throw new IllegalArgumentException("제목은 필수입니다.");

        if(request.getContent() == null) throw new IllegalArgumentException("본문은 필수입니다.");

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new QuestionHandler(ErrorStatus._USER_NOT_FOUND));

        Question question = converter.toEntity(request, user);
        questionRepository.save(question);

        for(String tagName : request.getHashtags()) {
            if(tagName == null)
                continue;

            // tag 가 존재하면 사용, 없으면 새로 저장해서 반환
            Hashtag hashtag = hashtagRepository.findByName(tagName)
                    .orElseGet(()-> hashtagRepository.save(Hashtag.builder()
                            .name(tagName).build()));

            QuestionHashtag questionHashtag = QuestionHashtag.builder()
                    .question(question)
                    .hashtag(hashtag)
                    .build();

            question.getQuestionHashtags().add(questionHashtag);
            questionHashtagRepository.save(questionHashtag);

        }

        return converter.toResponse(question);
    }

    @Override
    public List<AllQuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(converter::allQuestionResponse)
                .toList();
    }

    @Override
    public List<AllQuestionResponse> getQuestionsByUserId(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()->new QuestionHandler(ErrorStatus._USER_NOT_FOUND));

        return questionRepository.findByUser(user).stream()
                .map(converter::allQuestionResponse)
                .toList();
    }

    @Override
    public List<AllQuestionResponse> getQuestionsByHashtag(String hashtagName) {

        Hashtag hashtag = hashtagRepository.findByName(hashtagName)
                .orElseThrow(()->new QuestionHandler(ErrorStatus._HASHTAG_NOT_FOUND));

        List<QuestionHashtag> mapping = questionHashtagRepository.findAllByHashtag(hashtag);

        return mapping.stream().map(QuestionHashtag::getQuestion)
                .map(converter::allQuestionResponse)
                .toList();
    }

    @Override
    public QuestionResponse updateQuestion(Long questionId, QuestionRequest request, Long requestUserId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new QuestionHandler(ErrorStatus._QUESTION_NOT_FOUND));

        if(request.getTitle() == null || request.getContent() == null) {
            throw new IllegalArgumentException("제목과 본문은 필수입니다.");
        }

        question.updateQuestion(request.getTitle(), question.getContent());

        return converter.toResponse(question);
    }

    @Override
    public void deleteQuestion(Long questionId, Long requestUserId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new QuestionHandler(ErrorStatus._QUESTION_NOT_FOUND));

        if(!question.getUser().getUserId().equals(requestUserId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        questionRepository.deleteById(questionId);
    }
}
