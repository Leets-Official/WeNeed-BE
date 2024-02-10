package org.example.weneedbe.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public boolean isArticleBookmarkedByUser(Article article, User user){
        return bookmarkRepository.existsByArticleAndUser(article, user);
    }

    public int countBookmarkByArticle(Article article){
        return bookmarkRepository.countByArticle(article);
    }
}
