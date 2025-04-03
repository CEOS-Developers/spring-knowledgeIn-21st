package com.ceos21.ceos21BE.web.question.controller;

import com.ceos21.ceos21BE.apiPayload.ApiResponse;
import com.ceos21.ceos21BE.web.question.dto.request.QuestionRequest;
import com.ceos21.ceos21BE.web.question.dto.response.AllQuestionResponse;
import com.ceos21.ceos21BE.web.question.dto.response.QuestionResponse;
import com.ceos21.ceos21BE.web.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<QuestionResponse> createQuestion(
            @Valid @RequestBody QuestionRequest request
            ) {
        QuestionResponse response = questionService.createQuestion(request);
        return ApiResponse.onSuccess(response);
    }
    @PostMapping("/all")
    public ApiResponse<List<AllQuestionResponse>> getAllQuestions() {
        return ApiResponse.onSuccess(questionService.getAllQuestions());
    }

    @PostMapping("/user")
    public ApiResponse<List<AllQuestionResponse>> getByUser(@RequestBody Long userId) {
        return ApiResponse.onSuccess(questionService.getQuestionsByUserId(userId));
    }

    @PostMapping("/hashtag")
    public ApiResponse<List<AllQuestionResponse>> getByHashtag(@RequestBody String hashtagName) {
        return ApiResponse.onSuccess(questionService.getQuestionsByHashtag(hashtagName));
    }

    @PostMapping("/delete")
    public ApiResponse<String> deleteQuestion(@RequestBody Long questionId, @RequestBody Long requestUserId) {
        questionService.deleteQuestion(questionId, requestUserId);
        return ApiResponse.onSuccess("삭제 완료");
    }

    @PostMapping("/update")
    public ApiResponse<QuestionResponse> update(@RequestBody QuestionRequest request, @RequestBody Long questionId) {
        return ApiResponse.onSuccess(
                questionService.updateQuestion(questionId, request, request.getUserId())
        );
    }
}
