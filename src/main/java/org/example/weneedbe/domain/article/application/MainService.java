package org.example.weneedbe.domain.article.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ContentData;
import org.example.weneedbe.domain.article.dto.response.main.*;
import org.example.weneedbe.domain.article.exception.InvalidSortException;
import org.example.weneedbe.domain.article.repository.ArticleLikeRepository;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final static String SORT_BY_RECENT = "DESC";
    private final static String SORT_BY_HEARTS = "HEART";
    private final static String SORT_BY_VIEWS = "VIEW";

    public MainPortfolioDto getPortfolioArticleList(int size, int page, String sort, String[] detailTags) {
        User mockUser = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articlesPage = getSortedArticlesPage(sort, detailTags, pageable);
        PageableDto pageableDto = new PageableDto(size, page, articlesPage.getTotalPages(), articlesPage.getTotalElements());
        List<PortfolioArticleDto> articleList = convertToArticleDtoList(articlesPage.getContent(), mockUser);

        List<RecommendArticleDto> recommendArticleList = getRecommendArticleList(mockUser);
        List<HotArticleDto> hotArticleList = getHotArticleList();

        return new MainPortfolioDto(new MainUserDto(mockUser.getNickname()), pageableDto, hotArticleList, articleList, recommendArticleList);
    }

    private Page<Article> getSortedArticlesPage(String sort, String[] detailTags, Pageable pageable) {
        switch (sort) {
            case SORT_BY_RECENT:
                return articleRepository.findPortfoliosByDetailTagsInOrderByCreatedAtDesc(detailTags, pageable);
            case SORT_BY_HEARTS:
                return articleRepository.findPortfoliosByDetailTagsOrderByLikesDesc(detailTags, pageable);
            case SORT_BY_VIEWS:
                return articleRepository.findPortfoliosByDetailTagsInOrderByViewCountDesc(detailTags, pageable);
            default:
                throw new InvalidSortException();
        }
    }

    private PortfolioArticleDto convertToArticleDto(Article article, User user) {
        boolean isBookmarked = user != null && user.getBookmarks().contains(article);
        int heartCount = articleLikeRepository.countByArticle(article);

        return PortfolioArticleDto.builder()
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
                .limit(12)
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

    private List<PortfolioArticleDto> convertToArticleDtoList(List<Article> articles, User user) {
        return articles.stream()
                .map(article -> convertToArticleDto(article, user))
                .collect(Collectors.toList());
    }

    private List<HotArticleDto> getHotArticleList() {
        List<Article> articles = articleRepository.findPortfolios();
        List<WeightedArticleDto> weightedArticles = new ArrayList<>();

        for (Article article : articles) {
            double weight = calculateWeight(article);
            weightedArticles.add(new WeightedArticleDto(article, weight));
        }

        Collections.sort(weightedArticles);

        return weightedArticles.stream()
                .limit(5)
                .map(this::convertToHotArticleDto)
                .collect(Collectors.toList());
    }

    private double calculateWeight(Article article) {
        double viewCountWeight = 0.5 * article.getViewCount();
        double likeCountWeight = 1.0 * articleLikeRepository.countByArticle(article);
        double bookmarkCountWeight = 0.7 * bookmarkRepository.countByArticle(article);
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

    public MainRecruitDto getRecruitArticleList(int size, int page, String[] detailTags){
        User mockUser = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> recruitingPage = articleRepository.findRecruitingByDetailTagsInOrderByCreatedAtDesc(detailTags, pageable);
        PageableDto pageableDto = new PageableDto(size, page, recruitingPage.getTotalPages(), recruitingPage.getTotalElements());

        List<RecruitArticleDto> recruitList = convertToRecruitArticleDtoList(recruitingPage.getContent());

        return new MainRecruitDto(new MainUserDto(mockUser.getNickname()),pageableDto,recruitList);
    }
    private List<RecruitArticleDto> convertToRecruitArticleDtoList(List<Article> articles){
        return articles.stream()
                .map(article -> convertToRecruitArticleDto(article))
                .collect(Collectors.toList());
    }

    private RecruitArticleDto convertToRecruitArticleDto(Article article){
        int heartCount = articleLikeRepository.countByArticle(article);
        int bookmarkCount = bookmarkRepository.countByArticle(article);
        String contentByListContent = getContentByListContent(article.getContent());
        return RecruitArticleDto.builder()
                .articleId(article.getArticleId())
                .nickname(article.getUser().getNickname())
                .grade(article.getUser().getGrade())
                .major(article.getUser().getMajor())
                .thumbnail(article.getThumbnail())
                .title(article.getTitle())
                .content(contentByListContent)
                .createdAt(article.getCreatedAt())
                .viewCount(article.getViewCount())
                .bookmarkCount(bookmarkCount)
                .commentCount(article.getCommentList().size())
                .heartCount(heartCount).build();
    }
    private String getContentByListContent(List<ContentData> contentData){
        StringBuilder allContent = new StringBuilder();
        for(ContentData data : contentData){
            if("textData".equals(data.getType())){
                allContent.append(data.getTextData());
            }
        }
        return allContent.toString();
    }
}