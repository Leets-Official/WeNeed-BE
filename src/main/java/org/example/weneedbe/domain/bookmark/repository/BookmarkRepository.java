package org.example.weneedbe.domain.bookmark.repository;

import java.util.Optional;

import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByArticleAndUser(Article article, User user);

    int countByArticle(Article article);

    boolean existsByArticleAndUser(Article article, User user);
}