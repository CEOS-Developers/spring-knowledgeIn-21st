package com.ceos21.spring_boot.domain.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public Comments createComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    public Comments getCommentById(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 댓글이 없습니다 : " + id));
    }

    public void deleteComment(Long id) {
        Comments comment = getCommentById(id);
        commentsRepository.delete(comment);
    }
}
