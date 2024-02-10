package org.example.weneedbe.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public void bookmarkArticle(String authorizationHeader, Long articleId) {

        User user = userService.findUser(authorizationHeader);
        Article article = articleService.findArticle(articleId);

        Optional<Bookmark> bookmark = bookmarkRepository.findByArticleAndUser(article, user);

        if (bookmark.isEmpty()) {
            bookmarkRepository.save(new Bookmark(user, article));
        } else {
            bookmarkRepository.delete(bookmark.get());
        }
    }
    public boolean isArticleBookmarkedByUser(Article article, User user){
        return bookmarkRepository.existsByArticleAndUser(article, user);
    }

    public int countBookmarkByArticle(Article article){
        return bookmarkRepository.countByArticle(article);
    }
}
