package org.example.weneedbe.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.article.dto.response.main.PageableDto;
import org.example.weneedbe.domain.article.repository.ArticleLikeRepository;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.domain.user.dto.request.EditMyInfoRequest;
import org.example.weneedbe.domain.user.dto.request.UserInfoRequest;
import org.example.weneedbe.domain.user.dto.response.UserInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.BasicInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.MyPageArticleInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.EditMyInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.GetMyInfoResponse;
import org.example.weneedbe.domain.user.exception.InvalidProfileEditException;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserArticleRepository;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.jwt.TokenProvider;
import org.example.weneedbe.global.s3.application.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BookmarkRepository bookmarkRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final S3Service s3Service;
    private final UserArticleRepository userArticleRepository;

    public Boolean checkNicknameDuplicate(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

    public ResponseEntity<UserInfoResponse> setInfo(UserInfoRequest request, String authorizationHeader) throws Exception {
        User user = findUser(authorizationHeader);
        try {
            user.setUserInfo(request.getMajor(),
                    request.getDoubleMajor(),
                    request.getNickname(),
                    request.getUserGrade(),
                    request.getInterestField(),
                    true);

            userRepository.save(user);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        }
        return new ResponseEntity<>(new UserInfoResponse(true, "상세 정보 입력 성공"), HttpStatus.OK);
    }

    @Transactional
    public BasicInfoResponse getBasicInfo(int size, int page, String authorizationHeader, Long userId, Type articleType) {
        Long userIdFromHeader = findUser(authorizationHeader).getUserId();
        String userNickname = userRepository.findById(userIdFromHeader).orElseThrow(UserNotFoundException::new).getNickname();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserArticle> articlesPage = null;

        User user = null;

        if (userIdFromHeader.equals(userId)) {
            // '로그인한 사용자'의 정보 + MyOutput 노출
            user = userRepository.findById(userIdFromHeader).orElseThrow(UserNotFoundException::new);
            articlesPage = getArticlesFromUser(pageable, user, articleType);
            PageableDto pageableDto = new PageableDto(size, page, articlesPage.getTotalPages(), articlesPage.getTotalElements());
            List<MyPageArticleInfoResponse> myOutputList = convertToMyOutputList(articlesPage.getContent(), user);

            return setBasicInfoResponse(userNickname, true, userIdFromHeader, myOutputList, pageableDto);
        }
        // 주어진 userId의 사용자 정보 + MyOutput 노출
        user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        articlesPage = getArticlesFromUser(pageable, user, articleType);
        PageableDto pageableDto = new PageableDto(size, page, articlesPage.getTotalPages(), articlesPage.getTotalElements());
        List<MyPageArticleInfoResponse> myOutputList = convertToMyOutputList(articlesPage.getContent(), user);

        return setBasicInfoResponse(userNickname, false, userId, myOutputList, pageableDto);
    }

    private Page<UserArticle> getArticlesFromUser(Pageable pageable, User user, Type articletype) {
        return userArticleRepository.findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(user, articletype, pageable);
    }

    private List<MyPageArticleInfoResponse> convertToMyOutputList(List<UserArticle> myArticles, User user) {
        return myArticles.stream().map(s -> {
            List<String> teamProfiles = getTeamProfiles(s.getArticle().getArticleId());

            return new MyPageArticleInfoResponse(s.getArticle(),
                    articleLikeRepository.countByArticle(s.getArticle()),
                    teamProfiles);
        }).toList();
    }

    private BasicInfoResponse setBasicInfoResponse(String userNickname, Boolean sameUser, Long userId, List<MyPageArticleInfoResponse> myOutputList, PageableDto pageableDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        GetMyInfoResponse userInfo = GetMyInfoResponse.from(user);

        return BasicInfoResponse.of(userNickname, sameUser, userInfo, myOutputList, pageableDto);
    }

    public List<MyPageArticleInfoResponse> getOutputFromUser(User user, Type articleType) {
        List<UserArticle> myArticles = userArticleRepository.findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(
                user, articleType);

        return myArticles.stream().map(s -> {
            List<String> teamProfiles = getTeamProfiles(s.getArticle().getArticleId());

            return new MyPageArticleInfoResponse(s.getArticle(),
                    articleLikeRepository.countByArticle(s.getArticle()),
                    teamProfiles);
        }).toList();
    }

    public EditMyInfoResponse editInfo(String authorizationHeader, MultipartFile profileImage, EditMyInfoRequest request) throws IOException{
        User user = findUser(authorizationHeader);

        try {
            String profileImageUrl = s3Service.uploadImage(profileImage);

            user.editUserInfo(profileImageUrl,
                    request.getNickname(),
                    request.getUserGrade(),
                    request.getMajor(),
                    request.getDoubleMajor(),
                    request.getInterestField(),
                    request.getLinks(),
                    request.getSelfIntro());

            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new InvalidProfileEditException();
        }
        return EditMyInfoResponse.from(user);
    }

    public List<MyPageArticleInfoResponse> getBookmarkInfo(String authorizationHeader, Type articleType) {
        User user = findUser(authorizationHeader);

        List<Bookmark> recruitingBookmarks = bookmarkRepository.findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(
                user, articleType);

        return recruitingBookmarks.stream().map(s -> {
            List<String> teamProfiles = getTeamProfiles(s.getArticle().getArticleId());

            return new MyPageArticleInfoResponse(s.getArticle(),
                    articleLikeRepository.countByArticle(s.getArticle()),
                    teamProfiles);
        }).toList();
    }

    public List<MyPageArticleInfoResponse> getArticleInfo(String authorizationHeader, Type articleType) {
        User user = findUser(authorizationHeader);

        return getOutputFromUser(user, articleType);
    }

    // UserArticle 테이블에서 동일한 articleId에 속한 사용자들의 프로필 url을 List<String> 으로 반환하는 메서드
    private List<String> getTeamProfiles(Long articleId) {
        return userArticleRepository.findAllByArticle_ArticleId(articleId)
                .stream()
                .map(userArticle -> userArticle.getUser().getProfile())
                .toList();
    }

    public User findUser(String authorizationHeader){
        String token = tokenProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = tokenProvider.getUserIdFromToken(token);
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
