package org.example.weneedbe.domain.user.repository;

import org.example.weneedbe.domain.user.domain.UserArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleRepository extends JpaRepository<UserArticle, Long> {

  void deleteAllByArticle_ArticleId(Long article_articleId);
}
