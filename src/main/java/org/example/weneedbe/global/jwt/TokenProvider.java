package org.example.weneedbe.global.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.dto.response.TokenResponse;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.jwt.exception.ExpiredTokenException;
import org.example.weneedbe.global.jwt.exception.InvalidInputValueException;
import org.example.weneedbe.global.jwt.exception.InvalidTokenException;
import org.example.weneedbe.global.jwt.exception.TokenNotFoundException;
import org.example.weneedbe.global.jwt.service.RefreshTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class TokenProvider {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(User user) {
        return makeToken(ACCESS_TOKEN_DURATION, user);
    }
    public String generateRefreshToken(User user) {
        return makeToken(REFRESH_TOKEN_DURATION, user);
    }

    public String returnRefreshToken(User user) {
        String newRefreshToken = generateRefreshToken(user);
        return refreshTokenService.returnRefreshToken(user.getUserId(), newRefreshToken);
    }

    public String generateNewRefreshToken(User user) {
        String newRefreshToken = makeToken(REFRESH_TOKEN_DURATION, user);
        RefreshToken existingRefreshToken = refreshTokenRepository.findByUserId(user.getUserId())
                .orElseThrow(TokenNotFoundException::new);

        refreshTokenRepository.save(existingRefreshToken.update(newRefreshToken));
        return newRefreshToken;
    }

    private String makeToken(Duration duration, User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getUserId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 토큰의 유효성을 검증한다
    public boolean validToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        }
    }

    // 토큰 기반으로 유저 인증 정보를 가져온다
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);
    }

    // 토큰 기반으로 유저 ID를 가져온다
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getTokenFromAuthorizationHeader(String authorizationHeader){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidInputValueException();
        }
        return authorizationHeader.substring(7);
    }

    public TokenResponse regenerateToken(String refreshToken) {
        validToken(refreshToken);
        Long userId = getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return TokenResponse.builder()
                .accessToken(generateAccessToken(user))
                .refreshToken(generateNewRefreshToken(user))
                .build();
    }
}
