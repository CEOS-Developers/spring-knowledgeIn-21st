package com.ceos21.spring_boot.service;

import com.ceos21.spring_boot.domain.enums.LikeStatus;
import com.ceos21.spring_boot.dto.Answer.AnswerAndPostDTO;
import com.ceos21.spring_boot.dto.Answer.AnswerRequestDTO;
import com.ceos21.spring_boot.dto.Answer.AnswerResponseDTO;

public interface AnswerService {

    AnswerResponseDTO addAnswer(AnswerRequestDTO answerRequest);

    AnswerAndPostDTO getAnswersByPostId(Long postId);

}
