package org.example.weneedbe.domain.article.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.application.repository.RecruitRepository;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ContentData;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.article.dto.response.main.*;
import org.example.weneedbe.domain.article.exception.InvalidSortException;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.bookmark.service.BookmarkService;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.service.UserService;
import org.example.weneedbe.global.jwt.exception.InvalidInputValueException;
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
@Slf4j
public class MainService {
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final UserService userService;
    private final BookmarkService bookmarkService;
    private final RecruitRepository recruitRepository;
    private final static String SORT_BY_RECENT = "DESC";
    private final static String SORT_BY_HEARTS = "HEART";
    private final static String SORT_BY_VIEWS = "VIEW";

    public MainPortfolioDto getPortfolioArticleList(int size, int page, String sort, String tags, String authorizationHeader) {
        User user = null;
        String guestNickname = "guest";
        if (authorizationHeader != null) {
            user = userService.findUser(authorizationHeader);
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articlesPage = null;
        if ("ALL".equals(tags)) {
            articlesPage = getSortedArticlesPageAll(sort, pageable);
        }else {
            String[] detailTags = parseStringToTags(tags);
            articlesPage = getSortedArticlesPage(sort, detailTags, pageable);
        }

        PageableDto pageableDto = new PageableDto(size, page, articlesPage.getTotalPages(), articlesPage.getTotalElements());
        List<PortfolioArticleDto> articleList = convertToPortfolioArticleDtoList(articlesPage.getContent(), user);

        List<RecommendArticleDto> recommendArticleList = getRecommendArticleList(user);
        List<HotArticleDto> hotArticleList = getHotArticleList();
        MainUserDto userDto = new MainUserDto(user != null ? user.getNickname() : guestNickname, user != null ? user.getUserId() : -1L);

        return new MainPortfolioDto(userDto, pageableDto, hotArticleList, articleList, recommendArticleList);
    }

    private Page<Article> getSortedArticlesPageAll(String sort, Pageable pageable) {
        switch (sort) {
            case SORT_BY_RECENT:
                return articleRepository.findAllByTypeOrderByCreatedAtDesc(Type.PORTFOLIO,pageable);
            case SORT_BY_HEARTS:
                return articleRepository.findAllByPortfolioTypeOrderByLikesDesc(pageable);
            case SORT_BY_VIEWS:
                return articleRepository.findAllByPortfolioTypeOrderByViewCountDesc(pageable);
            default:
                throw new InvalidSortException();
        }
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
        boolean isBookmarked = user != null && containsBookmarkId(user.getBookmarks(), article.getArticleId());

        int heartCount = articleService.countHeartByArticle(article);

        return PortfolioArticleDto.builder()
                .articleId(article.getArticleId())
                .thumbnail(article.getThumbnail())
                .writerNickname(article.getUser().getNickname())
                .profile(article.getUser().getProfile())
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
        boolean isBookmarked = user != null && containsBookmarkId(user.getBookmarks(), article.getArticleId());
        return RecommendArticleDto.builder()
                .articleId(article.getArticleId())
                .thumbnail(article.getThumbnail())
                .isBookmarked(isBookmarked)
                .title(article.getTitle())
                .build();
    }

    private List<PortfolioArticleDto> convertToPortfolioArticleDtoList(List<Article> articles, User user) {
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
        double likeCountWeight = 1.0 * articleService.countHeartByArticle(article);
        double bookmarkCountWeight = 0.7 * bookmarkService.countBookmarkByArticle(article);
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

    public MainRecruitDto getRecruitArticleList(int size, int page, String tags, String authorizationHeader) {
        User user = null;
        String guestNickname = "guest";

        if (authorizationHeader != null) {
            user = userService.findUser(authorizationHeader);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> recruitingPage = null;
        if ("ALL".equals(tags)) {
            recruitingPage = articleRepository.findAllByTypeOrderByCreatedAtDesc(Type.RECRUITING,pageable);
        }else {
            String[] detailTags = parseStringToTags(tags);
            recruitingPage = articleRepository.findRecruitingByDetailTagsInOrderByCreatedAtDesc(detailTags, pageable);
        }
        PageableDto pageableDto = new PageableDto(size, page, recruitingPage.getTotalPages(), recruitingPage.getTotalElements());

        List<RecruitArticleDto> recruitList = convertToRecruitArticleDtoList(recruitingPage.getContent());
        MainUserDto userDto = new MainUserDto(user != null ? user.getNickname() : guestNickname, user != null ? user.getUserId() : -1L);

        return new MainRecruitDto(userDto, pageableDto, recruitList);
    }

    private List<RecruitArticleDto> convertToRecruitArticleDtoList(List<Article> articles) {
        return articles.stream()
                .map(article -> convertToRecruitArticleDto(article))
                .collect(Collectors.toList());
    }

    private RecruitArticleDto convertToRecruitArticleDto(Article article) {
        int heartCount = articleService.countHeartByArticle(article);
        int bookmarkCount = bookmarkService.countBookmarkByArticle(article);
        String contentByListContent = getContentByListContent(article.getContent());
        boolean isRecruiting = recruitRepository.existsByArticle_ArticleId(article.getArticleId());

        return RecruitArticleDto.builder()
                .articleId(article.getArticleId())
                .nickname(article.getUser().getNickname())
                .profile(article.getUser().getProfile())
                .grade(article.getUser().getGrade())
                .major(article.getUser().getMajor())
                .thumbnail(article.getThumbnail())
                .title(article.getTitle())
                .content(contentByListContent)
                .sharedText(article.getSharedText())
                .createdAt(article.getCreatedAt())
                .viewCount(article.getViewCount())
                .bookmarkCount(bookmarkCount)
                .commentCount(article.getCommentList().size())
                .heartCount(heartCount)
                .isRecruiting(isRecruiting).build();
    }

    private String getContentByListContent(List<ContentData> contentData) {
        StringBuilder allContent = new StringBuilder();
        for (ContentData data : contentData) {
            if ("text".equals(data.getType())) {
                allContent.append(data.getData()).append(" ");
            }
        }
        return allContent.toString().trim();
    }

    private boolean containsBookmarkId(List<Bookmark> bookmarks, Long articleId) {
        for (Bookmark bookmark : bookmarks) {
            if (bookmark.getArticle().getArticleId().equals(articleId)) {
                return true;
            }
        }
        return false;
    }

    public MainSearchDto getSearchArticleList(int size, int page, String keyword, String authorizationHeader){
        User user = null;
        String guestNickname = "guest";

        if (authorizationHeader != null) {
            user = userService.findUser(authorizationHeader);
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articlesPage = articleRepository.findAllByTitleOrTextDataContaining(keyword, pageable);
        PageableDto pageableDto = new PageableDto(size, page, articlesPage.getTotalPages(), articlesPage.getTotalElements());

        List<SearchArticleDto> articleList = convertToSearchArticleDtoList(articlesPage.getContent(), user);
        MainUserDto userDto = new MainUserDto(user != null ? user.getNickname() : guestNickname, user != null ? user.getUserId() : -1L);

        return new MainSearchDto(userDto, pageableDto, articleList);
    }

    private List<SearchArticleDto> convertToSearchArticleDtoList(List<Article> articlesPage, User user) {
        return articlesPage.stream()
                .map(article -> convertToSearchArticleDto(article, user))
                .collect(Collectors.toList());

    }

    private SearchArticleDto convertToSearchArticleDto(Article article, User user) {
        int heartCount = articleService.countHeartByArticle(article);
        int bookmarkCount = bookmarkService.countBookmarkByArticle(article);
        boolean isHearted = articleService.isArticleLikedByUser(article, user);
        boolean isBookmarked = bookmarkService.isArticleBookmarkedByUser(article, user);
        return new SearchArticleDto(article, heartCount, bookmarkCount, isBookmarked, isHearted);
    }

    private String[] parseStringToTags(String tags){
        if(tags == null || tags.isEmpty()){
            throw new InvalidInputValueException();
        }
        return tags.split(",");
    }
}