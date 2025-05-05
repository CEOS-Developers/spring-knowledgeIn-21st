package com.ceos21.knowledgeIn.controller.post;

import com.ceos21.knowledgeIn.controller.dto.post.CommentCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.CommentResponseDTO;
import com.ceos21.knowledgeIn.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class
CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public Long createComment(@RequestBody CommentCreateDTO dto) {
        return commentService.createComment(dto);
    }

    //게시글에 딸린 댓글 조회
    @GetMapping("/comments/post/{postId}")
    public List<CommentResponseDTO> findAllCommentsByPost(@PathVariable Long postId) {
        return commentService.findAllCommentsByPost(postId);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        List<CommentResponseDTO> comments = commentService.findAllComments();
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
