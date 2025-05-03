package com.ceos21.spring_knowledgeIn_21st.domain.answer.application;

import com.ceos21.spring_knowledgeIn_21st.domain.answer.dao.AnswerRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.answer.domain.Answer;
import com.ceos21.spring_knowledgeIn_21st.domain.answer.dto.request.AnswerAddRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.answer.dto.request.AnswerUpdateRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.post.dao.PostRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.post.domain.Post;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.dao.ReactionRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.Reaction;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.domain.ReactionType;
import com.ceos21.spring_knowledgeIn_21st.domain.reaction.dto.request.ReactionAddRequest;
import com.ceos21.spring_knowledgeIn_21st.domain.user.dao.UserRepository;
import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import com.ceos21.spring_knowledgeIn_21st.global.exception.CustomException;
import com.ceos21.spring_knowledgeIn_21st.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;

    /**
     * 답변 추가
     */
    @Transactional
    public Answer saveAnswer(AnswerAddRequest request, Long postId, Long userId) {
        // 작성자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 질문글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // 본인 질문글에는 답변 불가
        if (post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.CANNOT_ANSWER_OWN_POST);
        }

        Answer answer = request.toEntity(user);
        post.addAnswer(answer);
        return answerRepository.save(answer);
    }

    /**
     * 답변 조회
     */
    public Answer findAnswerById(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
    }

    /**
     * 답변 전체 조회
     */
    public List<Answer> findAllAnswers(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return answerRepository.findByPostId(postId);
    }

    /**
     * 답변 수정
     */
    @Transactional
    public Answer updateAnswer(Long answerId, AnswerUpdateRequest request, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        if (!answer.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ANSWER_ACCESS_DENIED);
        }

        answer.update(request.content());
        return answer;
    }

    /**
     * 답변 삭제
     */
    @Transactional
    public void deleteAnswer(Long answerId, Long userId) {
        Answer answer = findAnswerById(answerId);
        if (!answer.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ANSWER_ACCESS_DENIED);
        }
        answerRepository.deleteById(answerId);
    }

    /**
     * 답변 반응
     *
     * @param answerId
     * @param request
     * @param user
     * @return
     */
    @Transactional
    public String reactAnswer(Long answerId, ReactionAddRequest request, User user) {
        ReactionType type = request.type();
        Optional<Reaction> existingReaction = reactionRepository.findByUserIdAndAnswerId(user.getId(), answerId);
        Answer answer = findAnswerById(answerId);

        if (existingReaction.isEmpty()) {
            reactionRepository.save(request.toEntity(user, answer));
            addReaction(user, answer, type);
            return "반응을 추가했습니다";
        }
        else {
            Reaction reaction = existingReaction.get();
            if (type != reaction.getType()) {
                changeReaction(reaction, answer, type);
                return "반응을 변경했습니다";
            }
            else {
                removeReaction(reaction, answer);
                return "반응을 취소했습니다";
            }
        }
    }


    private void addReaction(User user, Answer answer, ReactionType type) {
        if (type == ReactionType.LIKE) {answer.increaseLikeCount();}
        else answer.increaseDislikeCount();
    }

    private void removeReaction(Reaction reaction, Answer answer) {
        reactionRepository.delete(reaction);
        if (reaction.getType() == ReactionType.LIKE) {answer.decreaseLikeCount();}
        else answer.decreaseDislikeCount();
    }

    private void changeReaction(Reaction reaction, Answer answer, ReactionType newType) {
        ReactionType oldType = reaction.getType();
        reaction.changeType(newType);
        reactionRepository.save(reaction);
        if (oldType == ReactionType.LIKE) {
            answer.decreaseLikeCount();
            answer.increaseDislikeCount();
        }
        else {
            answer.increaseLikeCount();
            answer.decreaseDislikeCount();
        }
    }
}