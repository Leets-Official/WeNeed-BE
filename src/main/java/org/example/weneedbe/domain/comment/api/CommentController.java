package org.example.weneedbe.domain.comment.api;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.comment.application.CommentService;
import org.example.weneedbe.domain.comment.dto.request.AddCommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{articleId}")
  public ResponseEntity<Void> createComment(@PathVariable Long articleId,
      @RequestBody AddCommentRequest request) {
    commentService.createComment(articleId, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{articleId}/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long articleId,
      @PathVariable Long commentId) {
    commentService.deleteComment(articleId, commentId);
    return ResponseEntity.ok().build();
  }
}
