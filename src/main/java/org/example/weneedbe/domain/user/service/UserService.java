package org.example.weneedbe.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.article.repository.ArticleLikeRepository;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.bookmark.repository.BookmarkRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.dto.request.UserInfoRequest;
import org.example.weneedbe.domain.user.dto.response.UserInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.MyPageArticleInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.MyPageGetMyInfoResponse;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BookmarkRepository bookmarkRepository;
    private final ArticleLikeRepository articleLikeRepository;

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

    public ResponseEntity<UserInfoResponse> getUserInfo(UserInfoRequest request) throws Exception {
        try {
            /* 토큰을 통한 user 객체를 불러옴 */
            /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
            User mockUser = userRepository.findById(1L).orElseThrow();
            mockUser.getUserInfo(request.getMajor(),
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

    public MyPageGetMyInfoResponse getMyInfo(String authorizationHeader) {
        /* 토큰을 통한 user 객체를 불러옴 */
        /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
/*        String token = tokenProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = tokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);*/

        User mockUser = userRepository.findById(1L).orElseThrow();

        return MyPageGetMyInfoResponse.from(mockUser);

    }

    public List<MyPageArticleInfoResponse> getInterestingCrewInfo(String authorizationHeader) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Bookmark> recruitingBookmarks = bookmarkRepository.findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(
            user, Type.RECRUITING);

        return recruitingBookmarks.stream().map(s -> new MyPageArticleInfoResponse(s.getArticle(),
            articleLikeRepository.countByArticle(s.getArticle()))).toList();
    }

    private Long getUserIdFromHeader(String authorizationHeader) {
        String token = tokenProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        return tokenProvider.getUserIdFromToken(token);
    }
}
