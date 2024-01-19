package org.example.weneedbe.domain.user.service;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.dto.request.UserInfoRequest;
import org.example.weneedbe.domain.user.dto.response.UserInfoResponse;
import org.example.weneedbe.domain.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Boolean checkNicknameDuplicate(String nickName) {
        return userRepository.existsByNickname(nickName);
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            mockUser = User.builder()
                    .email(mockUser.getEmail())
                    .major(request.getMajor())
                    .doubleMajor(request.getDoubleMajor())
                    .nickname(request.getNickname())
                    .grade(request.getUserGrade())
                    .interestField(request.getInterestField())
                    .hasRegistered(true)
                    .build();
            userRepository.save(mockUser);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        }
        return new ResponseEntity<>(new UserInfoResponse(true, "상세 정보 입력 성공"), HttpStatus.OK);
    }
}
