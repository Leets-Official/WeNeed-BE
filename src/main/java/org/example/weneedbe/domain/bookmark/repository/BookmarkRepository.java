package org.example.weneedbe.domain.bookmark.repository;

import java.util.Optional;

import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByArticleAndUser(Article article, User user);

    int countByArticle(Article article);

    boolean existsByArticleAndUser(Article article, User user);

    Page<Bookmark> findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(User user, @Param("articleType") Type articleType, Pageable pageable);

}