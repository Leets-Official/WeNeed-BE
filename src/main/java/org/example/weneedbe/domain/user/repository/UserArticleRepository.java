package org.example.weneedbe.domain.user.repository;

import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserArticleRepository extends JpaRepository<UserArticle, Long> {

    List<UserArticle> findAllByArticle_ArticleId(Long articleId);
    void deleteAllByArticle_ArticleId(Long article_articleId);
    Page<UserArticle> findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(User user, @Param("articleType") Type articleType, Pageable pageable);
}
