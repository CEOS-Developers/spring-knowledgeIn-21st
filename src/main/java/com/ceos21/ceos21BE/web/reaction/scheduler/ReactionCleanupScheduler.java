package com.ceos21.ceos21BE.web.reaction.scheduler;

import com.ceos21.ceos21BE.web.reaction.entity.enumtype.ReactionType;
import com.ceos21.ceos21BE.web.reaction.repository.ReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ReactionCleanupScheduler {

    private final ReactionRepository reactionRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void cleanUpOldNoneReactions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(30); // 30일 이전의 반응
        reactionRepository.deleteByReactionTypeAndCreatedAtBefore(ReactionType.NONE, cutoffTime);
    }
}
