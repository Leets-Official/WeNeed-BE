package org.example.weneedbe.global.config.jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.global.config.jwt.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidTokenException::new);
    }

    public void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }
}
