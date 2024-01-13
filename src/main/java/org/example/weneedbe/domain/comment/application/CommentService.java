package org.example.weneedbe.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.exception.ArticleNotFoundException;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.comment.dto.request.AddCommentRequest;
import org.example.weneedbe.domain.comment.repository.CommentRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  @Transactional
  public void createComment(Long articleId, AddCommentRequest request) {
    Article article = articleRepository.findById(articleId).orElseThrow(
        ArticleNotFoundException::new);

    /* 토큰을 통한 user 객체를 불러옴 */
    /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
    User mockUser = userRepository.findById(1L).orElseThrow();

    Comment comment = Comment.of(request, article, mockUser);

    /* 부모 댓글이 있는 경우 */
    if (request.getParentId() != 0) {
      Comment parentComment = commentRepository.findById(request.getParentId())
          .orElseThrow();
      parentComment.addChild(comment);
    } else {
      article.getCommentList().add(comment);
      commentRepository.save(comment);
    }
  }

  public void deleteComment(Long articleId, Long commentId) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(ArticleNotFoundException::new);

    Comment comment = commentRepository.findById(commentId).orElseThrow();

    /* 최상위 댓글일 경우 */
    if (comment.getParentId() == 0) {
      comment.deleteChild();
    } else {
      article.getCommentList().remove(comment);
    }

    commentRepository.delete(comment);
  }
}
