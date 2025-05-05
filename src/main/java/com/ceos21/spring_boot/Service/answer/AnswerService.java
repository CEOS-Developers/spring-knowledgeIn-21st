package com.ceos21.spring_boot.Service.answer;

import com.ceos21.spring_boot.DTO.answer.AnswerDTO;
import com.ceos21.spring_boot.Domain.post.Answer;
import com.ceos21.spring_boot.Repository.post.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public List<AnswerDTO> getAnswersByPostId(Long postId) {
        List<Answer> answers = answerRepository.findByPostId(postId);
        return answers.stream()
                .map(AnswerDTO::from)
                .toList();
    }
}
