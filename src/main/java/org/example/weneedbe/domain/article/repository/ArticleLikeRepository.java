package org.example.weneedbe.domain.article.repository;

import java.util.Optional;

import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ArticleLike;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findByArticleAndUser(Article article, User user);

    int countByArticle(Article article);

}
