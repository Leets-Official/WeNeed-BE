package org.example.weneedbe.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.exception.AuthorMismatchException;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.comment.dto.request.AddCommentRequest;
import org.example.weneedbe.domain.comment.exception.CommentNotFoundException;
import org.example.weneedbe.domain.comment.repository.CommentRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final ArticleService articleService;
  private final UserService userService;

  @Transactional
  public void createComment(String authorizationHeader, Long articleId, AddCommentRequest request) {
    Article article = articleService.findArticle(articleId);
    User user = userService.findUser(authorizationHeader);

    Comment comment = Comment.of(request, article, user);

    /* 부모 댓글이 있는 경우 */
    if (request.getParentId() != 0) {
      Comment parentComment = findComment(request.getParentId());
      parentComment.addChild(comment);
    } else {
      article.getCommentList().add(comment);
      commentRepository.save(comment);
    }
  }

  public void deleteComment(String authorizationHeader, Long articleId, Long commentId) {
    Article article = articleService.findArticle(articleId);
    User user = userService.findUser(authorizationHeader);

    Comment comment = findComment(commentId);

    validateUserOwnership(comment, user);

    /* 최상위 댓글일 경우 */
    if (comment.getParentId() == 0) {
      comment.deleteChild();
    } else {
      article.getCommentList().remove(comment);
    }

    commentRepository.delete(comment);
  }

  private Comment findComment(Long commentId){
    return commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);
  }

  private void validateUserOwnership(Comment comment, User user){
    if (!user.equals(comment.getWriter())) {
      throw new AuthorMismatchException();
    }
  }
}
