package org.example.weneedbe.global.config.jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.global.config.jwt.exception.InvalidTokenException;
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

    public void saveRefreshToken(Long userId, String newRefreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken existingRefreshToken = optionalRefreshToken.get();
            existingRefreshToken.update(newRefreshToken);
            return;
        }
        RefreshToken initialRefreshToken = new RefreshToken(userId, newRefreshToken);
        refreshTokenRepository.save(initialRefreshToken);
    }
}
