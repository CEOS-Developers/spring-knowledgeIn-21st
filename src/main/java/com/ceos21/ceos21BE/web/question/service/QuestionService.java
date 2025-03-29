package com.ceos21.ceos21BE.web.question.service;

import com.ceos21.ceos21BE.web.question.dto.request.QuestionRequest;
import com.ceos21.ceos21BE.web.question.dto.response.AllQuestionResponse;
import com.ceos21.ceos21BE.web.question.dto.response.QuestionResponse;

import java.util.List;


public interface QuestionService {
    public QuestionResponse createQuestion(QuestionRequest request);
    public List<AllQuestionResponse> getAllQuestions();

    public List<AllQuestionResponse> getQuestionsByUserId(Long userId);
    public List<AllQuestionResponse> getQuestionsByHashtag(String hashtagName);
    public QuestionResponse updateQuestion(Long questionId, QuestionRequest request, Long requestUserId);
    public void deleteQuestion(Long questionId, Long requestUserId);
}
