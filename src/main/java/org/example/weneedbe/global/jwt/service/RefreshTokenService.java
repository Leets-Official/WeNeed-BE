package org.example.weneedbe.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public String returnRefreshToken(User user, String newRefreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user); //유저의 refreshToken 보유 여부를 확인한다.

        if (optionalRefreshToken.isPresent()) { // 최초 로그인이 아닐 시
            RefreshToken existingRefreshToken = optionalRefreshToken.get();
            return existingRefreshToken.getRefreshToken();
        }
        //최초 로그인 시
        RefreshToken initialRefreshToken = new RefreshToken(user, newRefreshToken);
        refreshTokenRepository.save(initialRefreshToken);
        return newRefreshToken;
    }
}
