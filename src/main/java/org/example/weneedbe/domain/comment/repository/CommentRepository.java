package org.example.weneedbe.domain.comment.repository;

import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findAllByArticle(Article article);

}
