package org.example.weneedbe.domain.token.repository;

import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
