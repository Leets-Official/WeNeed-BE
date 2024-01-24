package org.example.weneedbe.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.article.repository.ArticleLikeRepository;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.domain.user.dto.request.EditMyInfoRequest;
import org.example.weneedbe.domain.user.dto.request.UserInfoRequest;
import org.example.weneedbe.domain.user.dto.response.UserInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.MyPageArticleInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.EditMyInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.GetMyInfoResponse;
import org.example.weneedbe.domain.user.exception.InvalidProfileEditException;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserArticleRepository;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.jwt.TokenProvider;
import org.example.weneedbe.global.s3.application.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

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

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    public ResponseEntity<UserInfoResponse> setUserInfo(UserInfoRequest request) throws Exception {
        try {
            /* 토큰을 통한 user 객체를 불러옴 */
            /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
            User mockUser = userRepository.findById(1L).orElseThrow();
            mockUser.setUserInfo(request.getMajor(),
                    request.getDoubleMajor(),
                    request.getNickname(),
                    request.getUserGrade(),
                    request.getInterestField(),
                    true);

            userRepository.save(mockUser);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        }
        return new ResponseEntity<>(new UserInfoResponse(true, "상세 정보 입력 성공"), HttpStatus.OK);
    }

    public GetMyInfoResponse getMyInfo(String authorizationHeader) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return GetMyInfoResponse.from(user);
    }

    public EditMyInfoResponse editMyInfo(String authorizationHeader, MultipartFile profileImage, EditMyInfoRequest request) throws IOException{
        Long userId = getUserIdFromHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

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

    public List<MyPageArticleInfoResponse> getInterestingCrewInfo(String authorizationHeader) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Bookmark> recruitingBookmarks = bookmarkRepository.findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(
                user, Type.RECRUITING);

        return recruitingBookmarks.stream().map(s -> {
            List<String> teamProfiles = getTeamProfiles(s.getArticle().getArticleId());

            return new MyPageArticleInfoResponse(s.getArticle(),
                    articleLikeRepository.countByArticle(s.getArticle()),
                    teamProfiles);
        }).collect(Collectors.toList());
    }

    public List<MyPageArticleInfoResponse> getMyInfo(String authorizationHeader, Type articleType) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<UserArticle> myArticles = userArticleRepository.findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(
                user, articleType);

        return myArticles.stream().map(s -> {
            List<String> teamProfiles = getTeamProfiles(s.getArticle().getArticleId());

            return new MyPageArticleInfoResponse(s.getArticle(),
                    articleLikeRepository.countByArticle(s.getArticle()),
                    teamProfiles);
        }).collect(Collectors.toList());
    }

    // UserArticle 테이블에서 동일한 articleId에 속한 사용자들의 프로필 url을 List<String> 으로 반환하는 메서드
    private List<String> getTeamProfiles(Long articleId) {
        return userArticleRepository.findAllByArticle_ArticleId(articleId)
                .stream()
                .map(userArticle -> userArticle.getUser().getProfile())
                .collect(Collectors.toList());
    }

    private Long getUserIdFromHeader(String authorizationHeader) {
        String token = tokenProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        return tokenProvider.getUserIdFromToken(token);
    }
}
