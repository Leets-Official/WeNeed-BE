package org.example.weneedbe.domain.article.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.dto.response.main.*;
import org.example.weneedbe.domain.article.exception.InvalidSortException;
import org.example.weneedbe.domain.article.repository.ArticleLikeRepository;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;

    public MainPortfolioDto getMainArticleList(int size, int page, String sort, List<String> detailTags) {
        User mockUser = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articlesPage = getSortedArticlesPage(sort, detailTags, pageable);
        PageableDto pageableDto = new PageableDto(size, page, articlesPage.getTotalPages(), articlesPage.getTotalElements());

        List<ArticleDto> articleList = articlesPage.getContent()
                .stream()
                .map(article -> convertToArticleDto(article, mockUser))
                .collect(Collectors.toList());

        List<RecommendArticleDto> recommendArticleList = getRecommendArticleList(mockUser);
        List<HotArticleDto> hotArticleList = getHotArticleList();

        return new MainPortfolioDto(new MainUserDto(mockUser.getNickname()), pageableDto, hotArticleList, articleList, recommendArticleList);
    }

    private Page<Article> getSortedArticlesPage(String sort, List<String> detailTags, Pageable pageable) {
        switch (sort) {
            case "최신순":
                return articleRepository.findPortfoliosByDetailTagsInOrderByCreatedAtDesc(detailTags, pageable);
            case "좋아요순":
                return articleRepository.findPortfoliosByDetailTagsOrderByLikesDesc(detailTags, pageable);
            case "조회수순":
                return articleRepository.findPortfoliosByDetailTagsInOrderByViewCountDesc(detailTags, pageable);
            default:
                throw new InvalidSortException();
        }
    }

    private ArticleDto convertToArticleDto(Article article, User user) {
        boolean isBookmarked = user != null && user.getBookmarks().contains(article);
        int heartCount = articleLikeRepository.countByArticle(article);

        return ArticleDto.builder()
                .articleId(article.getArticleId())
                .thumbnail(article.getThumbnail())
                .writerNickname(article.getUser().getNickname())
                .heartCount(heartCount)
                .viewCount(article.getViewCount())
                .isBookmarked(isBookmarked)
                .build();
    }

    private List<RecommendArticleDto> getRecommendArticleList(User user) {
        List<Article> randomArticles = articleRepository.findRandomPortfolios();
        return randomArticles.stream()
                .limit(6)
                .map(article -> convertToRecommendArticleDto(article, user))
                .collect(Collectors.toList());
    }

    private RecommendArticleDto convertToRecommendArticleDto(Article article, User user) {
        boolean isBookmarked = user != null && user.getBookmarks().contains(article);
        return RecommendArticleDto.builder()
                .articleId(article.getArticleId())
                .thumbnail(article.getThumbnail())
                .isBookmarked(isBookmarked)
                .title(article.getTitle())
                .build();
    }

    public List<HotArticleDto> getHotArticleList() {
        List<Article> articles = articleRepository.findPortfolios();
        List<WeightedArticleDto> weightedArticles = articles.stream()
                .map(article -> new WeightedArticleDto(article, calculateWeight(article)))
                .sorted()
                .limit(5)
                .collect(Collectors.toList());

        return weightedArticles.stream()
                .map(this::convertToHotArticleDto)
                .collect(Collectors.toList());
    }

    private double calculateWeight(Article article) {
        double viewCountWeight = 0.7 * article.getViewCount();
        double likeCountWeight = 1.0 * articleLikeRepository.countByArticle(article);
        double bookmarkCountWeight = 0.5 * article.getViewCount();
        return viewCountWeight + likeCountWeight + bookmarkCountWeight;
    }

    private HotArticleDto convertToHotArticleDto(WeightedArticleDto weightedArticleDto) {
        Article article = weightedArticleDto.getArticle();
        return HotArticleDto.builder()
                .articleId(article.getArticleId())
                .thumbnail(article.getThumbnail())
                .title(article.getTitle())
                .build();
    }
}