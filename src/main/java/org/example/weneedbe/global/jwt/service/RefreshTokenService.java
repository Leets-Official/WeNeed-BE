package org.example.weneedbe.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.global.jwt.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidTokenException::new);
    }

    public String returnRefreshToken(Long userId, String newRefreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId); //유저의 refreshToken 보유 여부를 확인한다.

        if (optionalRefreshToken.isPresent()) { // 최초 로그인이 아닐 시
            RefreshToken existingRefreshToken = optionalRefreshToken.get();
            return existingRefreshToken.getRefreshToken();
        }
        //최초 로그인 시
        RefreshToken initialRefreshToken = new RefreshToken(userId, newRefreshToken);
        refreshTokenRepository.save(initialRefreshToken);
        return newRefreshToken;
    }

    public String generateNewRefreshToken(Long userId, String newRefreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId);

        RefreshToken existingRefreshToken = optionalRefreshToken.get();

        refreshTokenRepository.save(existingRefreshToken.update(newRefreshToken));
        return newRefreshToken;
    }
}
