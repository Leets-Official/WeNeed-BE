package org.example.weneedbe.domain.user.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.dto.request.UserInfoRequest;
import org.example.weneedbe.domain.user.dto.response.UserInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.MyPageGetMyInfoResponse;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;

import org.example.weneedbe.global.config.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

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

        return MyPageGetMyInfoResponse.builder()
                .profile(mockUser.getProfile())
                .nickname(mockUser.getNickname())
                .major(mockUser.getMajor())
                .userGrade(mockUser.getGrade())
                .doubleMajor(mockUser.getDoubleMajor())
                .interestField(mockUser.getInterestField())
                .email(mockUser.getEmail())
                .links(mockUser.getLinks())
                .selfIntro(mockUser.getAboutMe())
                .build();
    }
}
