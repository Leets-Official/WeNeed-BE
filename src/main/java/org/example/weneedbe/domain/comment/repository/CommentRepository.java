package org.example.weneedbe.domain.comment.repository;

import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);
}
