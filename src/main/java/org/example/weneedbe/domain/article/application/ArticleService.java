package org.example.weneedbe.domain.article.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ArticleLike;
import org.example.weneedbe.domain.article.dto.request.ArticleRequest;
import org.example.weneedbe.domain.article.dto.response.DetailResponseDto.DetailPortfolioDto;
import org.example.weneedbe.domain.article.dto.response.DetailResponseDto.CommentResponseDto;
import org.example.weneedbe.domain.article.dto.response.DetailResponseDto.WorkPortfolioArticleDto;
import org.example.weneedbe.domain.article.dto.response.DetailResponseDto.DetailRecruitDto;
import org.example.weneedbe.domain.article.dto.response.MemberInfoResponse;
import org.example.weneedbe.domain.article.exception.ArticleNotFoundException;
import org.example.weneedbe.domain.article.exception.AuthorMismatchException;
import org.example.weneedbe.domain.article.repository.ArticleLikeRepository;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.comment.repository.CommentRepository;
import org.example.weneedbe.domain.file.repository.FileRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserArticleRepository;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.jwt.TokenProvider;
import org.example.weneedbe.global.s3.application.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ArticleLikeRepository articleLikeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TokenProvider tokenProvider;
    private final CommentRepository commentRepository;
    private final UserArticleRepository userArticleRepository;
    private final FileRepository fileRepository;

    public void createPortfolio(MultipartFile thumbnail, List<MultipartFile> images,
                                List<MultipartFile> files, ArticleRequest request) throws IOException {

        String thumbnailUrl = s3Service.uploadImage(thumbnail);
        List<String> imageUrls = s3Service.uploadImages(images);
        List<String> fileUrls = s3Service.uploadFile(files);

        /* 토큰을 통한 user 객체를 불러옴 */
        /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
        User mockUser = userRepository.findById(1L).orElseThrow();

        Article article = Article.of(thumbnailUrl, imageUrls, fileUrls, request, mockUser);

        List<UserArticle> userArticles = setUserArticle(mockUser, request, article);
        article.setUserArticles(userArticles);

        articleRepository.save(article);
    }

    public void createRecruit(MultipartFile thumbnail, List<MultipartFile> images,
                              List<MultipartFile> files, ArticleRequest request) throws IOException {

        String thumbnailUrl = s3Service.uploadImage(thumbnail);
        List<String> imageUrls = s3Service.uploadImages(images);
        List<String> fileUrls = s3Service.uploadFile(files);

        /* 토큰을 통한 user 객체를 불러옴 */
        /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
        User mockUser = userRepository.findById(1L).orElseThrow();

        Article article = Article.of(thumbnailUrl, imageUrls, fileUrls, request, mockUser);
        article.setUserArticles(List.of(new UserArticle(mockUser, article)));

        articleRepository.save(article);
    }

    public List<MemberInfoResponse> getMemberList(String nickname) {
        return userRepository.findAllByNicknameStartingWith(nickname).stream()
                .map(MemberInfoResponse::new).toList();
    }

    public void likeArticle(Long articleId) {
        /* 토큰을 통한 user 객체를 불러옴 */
        /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
        User mockUser = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);

        Optional<ArticleLike> articleLike = articleLikeRepository.findByArticleAndUser(article,
                mockUser);

        if (articleLike.isEmpty()) {
            articleLikeRepository.save(new ArticleLike(mockUser, article));
        } else {
            articleLikeRepository.delete(articleLike.get());
        }
    }

    public void bookmarkArticle(Long articleId) {
        /* 토큰을 통한 user 객체를 불러옴 */
        /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
        User mockUser = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);

        Optional<Bookmark> bookmark = bookmarkRepository.findByArticleAndUser(article,
                mockUser);

        if (bookmark.isEmpty()) {
            bookmarkRepository.save(new Bookmark(mockUser, article));
        } else {
            bookmarkRepository.delete(bookmark.get());
        }
    }

    public DetailPortfolioDto getDetailPortfolio(String authorizationHeader, Long articleId) {
        Long userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);

        article.plusViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        int heartCount = articleLikeRepository.countByArticle(article);
        int bookmarkCount = bookmarkRepository.countByArticle(article);

        boolean isHearted = articleLikeRepository.existsByArticleAndUser(article, user);
        boolean isBookmarked = bookmarkRepository.existsByArticleAndUser(article, user);

        List<Article> portfolioArticlesByUser = articleRepository.findPortfolioArticlesByUser(article.getUser());
        List<Comment> commentList = commentRepository.findAllByArticle(article);
        List<CommentResponseDto> commentResponseDtos = mapToResponseDto(commentList);

        List<WorkPortfolioArticleDto> workList = initializeWorkList(portfolioArticlesByUser, article, user);

        return new DetailPortfolioDto(article, user, heartCount, bookmarkCount, workList, isHearted, isBookmarked, commentResponseDtos);
    }
    private List<WorkPortfolioArticleDto> initializeWorkList(List<Article> userPortfolio, Article article, User user) {
        List<WorkPortfolioArticleDto> workList = new ArrayList<>();
        for (Article portfolio : userPortfolio) {
            /* 상세조회하는 게시물 제외하고 workList에 추가 */
            if (!portfolio.getArticleId().equals(article.getArticleId())) {
                boolean bookmarked = bookmarkRepository.existsByArticleAndUser(portfolio, user);
                workList.add(new WorkPortfolioArticleDto(portfolio, bookmarked));
            }
        }
        return workList;
    }

    private Long getUserIdFromAuthorizationHeader(String authorizationHeader) {
        String token = tokenProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userIdFromToken = tokenProvider.getUserIdFromToken(token);
        return userIdFromToken;
    }

    public DetailRecruitDto getDetailRecruit(String authorizationHeader, Long articleId) {
        Long userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);

        article.plusViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        int heartCount = articleLikeRepository.countByArticle(article);
        int bookmarkCount = bookmarkRepository.countByArticle(article);

        boolean isHearted = articleLikeRepository.existsByArticleAndUser(article, user);
        boolean isBookmarked = bookmarkRepository.existsByArticleAndUser(article, user);

        List<Comment> commentList = commentRepository.findAllByArticle(article);
        List<CommentResponseDto> commentResponseDtos = mapToResponseDto(commentList);

        return new DetailRecruitDto(article, user, heartCount, bookmarkCount, isHearted, isBookmarked, commentResponseDtos);
    }

    public static List<CommentResponseDto> mapToResponseDto(List<Comment> commentList) {
        return commentList.stream()
                .filter(comment -> comment.getParentId() == 0)
                .map(comment -> new CommentResponseDto(comment, commentList))
                .collect(Collectors.toList());
    }

    @Transactional
    public void editPortfolio(String authorizationHeader, Long articleId, MultipartFile thumbnail,
        List<MultipartFile> images, List<MultipartFile> files, ArticleRequest request)
        throws IOException {

        Long userId = getUserIdFromAuthorizationHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId)
            .orElseThrow(ArticleNotFoundException::new);

        if (!user.equals(article.getUser())) {
            throw new AuthorMismatchException();
        }

        /* 기존 데이터 삭제 */
        userArticleRepository.deleteAllByArticle_ArticleId(articleId);
        fileRepository.deleteAllByArticle_ArticleId(articleId);

        String thumbnailUrl = s3Service.uploadImage(thumbnail);
        List<String> imageUrls = s3Service.uploadImages(images);
        List<String> fileUrls = s3Service.uploadFile(files);

        List<UserArticle> updatedUserArticles = setUserArticle(user, request, article);

        article.update(thumbnailUrl, imageUrls, fileUrls, request, updatedUserArticles);

        articleRepository.save(article);
    }

    private List<UserArticle> setUserArticle(User user, ArticleRequest request, Article article) {
        List<UserArticle> userArticles = new ArrayList<>();
        userArticles.add(new UserArticle(user, article));

        if (!request.getTeamMembersId().isEmpty()) {
            List<User> teamMembers = userRepository.findAllById(request.getTeamMembersId());

            userArticles.addAll(
                teamMembers.stream()
                    .map(member -> new UserArticle(member, article))
                    .toList()
            );
        }
        return userArticles;
    }
}
