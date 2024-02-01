package org.example.weneedbe.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.comment.application.CommentService;
import org.example.weneedbe.domain.comment.dto.request.AddCommentRequest;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 작성", description = "리크루팅 게시물에 댓글을 작성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/{articleId}")
  public ResponseEntity<Void> createComment(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable Long articleId, @RequestBody AddCommentRequest request) {
    commentService.createComment(authorizationHeader, articleId, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "댓글 삭제", description = "사용자가 댓글을 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("/{articleId}/{commentId}")
  public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable Long articleId, @PathVariable Long commentId) {
    commentService.deleteComment(authorizationHeader, articleId, commentId);
    return ResponseEntity.ok().build();
  }
}
