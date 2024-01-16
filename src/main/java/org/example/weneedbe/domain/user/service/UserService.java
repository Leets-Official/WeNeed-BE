package org.example.weneedbe.domain.user.service;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.config.jwt.TokenProvider;
import org.example.weneedbe.global.config.oauth.OAuthService;

import org.example.weneedbe.global.config.oauth.dto.OAuthToken;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuthService oAuthService;
    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, OAuthService oAuthService, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.oAuthService = oAuthService;
        this.tokenProvider = tokenProvider;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    public String googleOauthLogin(String code) {
        ResponseEntity<String> accessTokenResponse = oAuthService.createPostRequest(code);
        OAuthToken oAuthToken = oAuthService.getAccessToken(accessTokenResponse);

        ResponseEntity<String> userInfoResponse = oAuthService.createGetRequest(oAuthToken);
        String email = oAuthService.getUserEmail(userInfoResponse);
        User user = userRepository.findByEmail(email).orElse(null);

        saveUser(user, email);
        return tokenProvider.generateAccessToken(user);
    }

    private void saveUser(User user, String email) {

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .hasRegistered(false)
                    .build();
            userRepository.save(user);

            String refreshToken = tokenProvider.generateRefreshToken(user);
            saveRefreshToken(user.getUserId(), refreshToken);
        }
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }
}
