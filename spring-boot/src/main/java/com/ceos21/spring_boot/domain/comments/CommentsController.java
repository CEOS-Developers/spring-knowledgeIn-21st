package com.ceos21.spring_boot.domain.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping
    public Comments createComment(@RequestBody Comments comment) {
        return commentsService.createComment(comment);
    }

    @GetMapping
    public List<Comments> getAllComments() {
        return commentsService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comments getCommentById(@PathVariable Long id) {
        return commentsService.getCommentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentsService.deleteComment(id);
    }
}
