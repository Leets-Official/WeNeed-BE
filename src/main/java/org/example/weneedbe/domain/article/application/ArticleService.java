package org.example.weneedbe.domain.article.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.domain.Recruit;
import org.example.weneedbe.domain.application.exception.RecruitNotFoundException;
import org.example.weneedbe.domain.application.repository.RecruitRepository;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ArticleLike;
import org.example.weneedbe.domain.article.domain.ContentData;
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
import org.example.weneedbe.domain.bookmark.service.BookmarkService;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.comment.repository.CommentRepository;
import org.example.weneedbe.domain.file.domain.File;
import org.example.weneedbe.domain.file.dto.FileUploadDto;
import org.example.weneedbe.domain.file.repository.FileRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.domain.user.repository.UserArticleRepository;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.domain.user.service.UserService;
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
    private final CommentRepository commentRepository;
    private final UserArticleRepository userArticleRepository;
    private final FileRepository fileRepository;
    private final UserService userService;
    private final BookmarkService bookmarkService;
    private final BookmarkRepository bookmarkRepository;
    private final RecruitRepository recruitRepository;

    public void createPortfolio(String authorizationHeader, MultipartFile thumbnail, List<MultipartFile> images,
                                List<MultipartFile> files, ArticleRequest request) throws IOException {

        String thumbnailUrl = uploadThumbnail(thumbnail);
        List<String> imageUrls = uploadImages(images);
        List<FileUploadDto> fileUrls = uploadFiles(files);

        User user = userService.findUser(authorizationHeader);

        Article article = Article.of(thumbnailUrl, imageUrls, fileUrls, request, user);

        List<UserArticle> userArticles = setUserArticle(user, request, article);
        article.setUserArticles(userArticles);

        articleRepository.save(article);
    }

    public void createRecruit(String authorizationHeader, MultipartFile thumbnail, List<MultipartFile> images,
                              List<MultipartFile> files, ArticleRequest request) throws IOException {

        String thumbnailUrl = uploadThumbnail(thumbnail);
        List<String> imageUrls = uploadImages(images);
        List<FileUploadDto> fileUrls = uploadFiles(files);

        User user = userService.findUser(authorizationHeader);

        Article article = Article.of(thumbnailUrl, imageUrls, fileUrls, request, user);
        article.setUserArticles(List.of(new UserArticle(user, article)));

        articleRepository.save(article);
    }

    public List<MemberInfoResponse> getMemberList(String nickname) {
        return userRepository.findAllByNicknameStartingWith(nickname).stream()
                .map(MemberInfoResponse::new).toList();
    }

    public void likeArticle(String authorizationHeader, Long articleId) {

        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        Optional<ArticleLike> articleLike = articleLikeRepository.findByArticleAndUser(article, user);

        if (articleLike.isEmpty()) {
            articleLikeRepository.save(new ArticleLike(user, article));
        } else {
            articleLikeRepository.delete(articleLike.get());
        }
    }

    public void bookmarkArticle(String authorizationHeader, Long articleId) {

        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        Optional<Bookmark> bookmark = bookmarkRepository.findByArticleAndUser(article, user);

        if (bookmark.isEmpty()) {
            bookmarkRepository.save(new Bookmark(user, article));
        } else {
            bookmarkRepository.delete(bookmark.get());
        }
    }


    public DetailPortfolioDto getDetailPortfolio(String authorizationHeader, Long articleId) {
        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        article.plusViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        int heartCount = countHeartByArticle(article);
        int bookmarkCount = bookmarkService.countBookmarkByArticle(article);

        boolean isHearted = isArticleLikedByUser(article, user);
        boolean isBookmarked = bookmarkService.isArticleBookmarkedByUser(article, user);

        List<Article> portfolioArticlesByUser = articleRepository.findPortfolioArticlesByUser(article.getUser());
        List<Comment> commentList = commentRepository.findAllByArticle(article);
        List<CommentResponseDto> commentResponseDtos = mapToResponseDto(commentList);

        List<WorkPortfolioArticleDto> workList = initializeWorkList(portfolioArticlesByUser, article, user);

        boolean isRecruiting = recruitRepository.existsByArticle_ArticleId(articleId);

        return new DetailPortfolioDto(article, user, heartCount, bookmarkCount, workList, isHearted, isBookmarked, commentResponseDtos, isRecruiting);
    }
    private List<WorkPortfolioArticleDto> initializeWorkList(List<Article> userPortfolio, Article article, User user) {
        List<WorkPortfolioArticleDto> workList = new ArrayList<>();
        for (Article portfolio : userPortfolio) {
            /* 상세조회하는 게시물 제외하고 workList에 추가 */
            if (!portfolio.getArticleId().equals(article.getArticleId())) {
                boolean bookmarked = bookmarkService.isArticleBookmarkedByUser(portfolio, user);
                workList.add(new WorkPortfolioArticleDto(portfolio, bookmarked));
            }
        }
        return workList;
    }

    public DetailRecruitDto getDetailRecruit(String authorizationHeader, Long articleId) {
        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        article.plusViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        int heartCount = countHeartByArticle(article);
        int bookmarkCount = bookmarkService.countBookmarkByArticle(article);

        boolean isHearted = isArticleLikedByUser(article, user);
        boolean isBookmarked = bookmarkService.isArticleBookmarkedByUser(article, user);

        List<Comment> commentList = commentRepository.findAllByArticle(article);
        List<CommentResponseDto> commentResponseDtos = mapToResponseDto(commentList);

        boolean isRecruiting = recruitRepository.existsByArticle_ArticleId(articleId);

        return new DetailRecruitDto(article, user, heartCount, bookmarkCount, isHearted, isBookmarked, commentResponseDtos, isRecruiting);
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

        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        validateUserOwnership(article, user);

        /* 기존 데이터 삭제 */
        userArticleRepository.deleteAllByArticle_ArticleId(articleId);
        deleteS3Thumbnail(article);
        deleteS3ContentImages(article);
        deleteS3Files(articleId);

        String thumbnailUrl = uploadThumbnail(thumbnail);
        List<String> imageUrls = uploadImages(images);
        List<FileUploadDto> fileUrls = uploadFiles(files);

        List<UserArticle> updatedUserArticles = setUserArticle(user, request, article);

        article.updatePortfolio(thumbnailUrl, imageUrls, fileUrls, request, updatedUserArticles);

        articleRepository.save(article);
    }

    @Transactional
    public void editRecruit(String authorizationHeader, Long articleId, MultipartFile thumbnail,
        List<MultipartFile> images, List<MultipartFile> files, ArticleRequest request)
        throws IOException {

        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        validateUserOwnership(article, user);

        /* 기존 데이터 삭제 */
        deleteS3Thumbnail(article);
        deleteS3ContentImages(article);
        deleteS3Files(articleId);

        String thumbnailUrl = uploadThumbnail(thumbnail);
        List<String> imageUrls = uploadImages(images);
        List<FileUploadDto> fileUrls = uploadFiles(files);

        article.updateRecruit(thumbnailUrl, imageUrls, fileUrls, request);

        articleRepository.save(article);
    }

    private String uploadThumbnail(MultipartFile thumbnail) throws IOException {
        return s3Service.uploadImage(thumbnail);
    }

    private List<String> uploadImages(List<MultipartFile> images) throws IOException {
        if (images == null) {
            return Collections.emptyList();
        }
        return s3Service.uploadImages(images);
    }

    private List<FileUploadDto> uploadFiles(List<MultipartFile> files) {
        if (files == null) {
            return Collections.emptyList();
        }
        return s3Service.uploadFiles(files);
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

    public void deleteArticle(String authorizationHeader, Long articleId){
        User user = userService.findUser(authorizationHeader);
        Article article = findArticle(articleId);

        validateUserOwnership(article, user);

        articleRepository.delete(article);
    }

    public Article findArticle(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private void validateUserOwnership(Article article, User user){
        if (!user.equals(article.getUser())) {
            throw new AuthorMismatchException();
        }
    }
    public boolean isArticleLikedByUser(Article article, User user){
        return articleLikeRepository.existsByArticleAndUser(article, user);
    }

    public int countHeartByArticle(Article article){
        return articleLikeRepository.countByArticle(article);
    }

    private void deleteS3Thumbnail(Article article) {
        s3Service.deleteFile(article.getThumbnail());
    }

    private void deleteS3ContentImages(Article article) {
        List<ContentData> content = article.getContent();
        content.stream().filter(s -> "image".equals(s.getType())).map(ContentData::getData).forEach(s3Service::deleteFile);
    }

    private void deleteS3Files(Long articleId) {
        List<File> fileList = fileRepository.findAllByArticle_ArticleId(articleId);
        fileList.stream().map(File::getFileUrl).forEach(s3Service::deleteFile);
        fileRepository.deleteAllByArticle_ArticleId(articleId);
    }
}