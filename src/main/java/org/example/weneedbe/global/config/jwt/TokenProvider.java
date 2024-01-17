package org.example.weneedbe.global.config.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.domain.RefreshToken;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.global.config.jwt.exception.ExpiredTokenException;
import org.example.weneedbe.global.config.jwt.exception.InvalidInputValueException;
import org.example.weneedbe.global.config.jwt.exception.InvalidTokenException;
import org.example.weneedbe.global.config.jwt.service.RefreshTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    public String generateAccessToken(User user) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis()), user);
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();

        String newRefreshToken = makeToken(new Date(now.getTime() + REFRESH_TOKEN_DURATION.toMillis()), user);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(user.getUserId());
        if (refreshToken.isEmpty()) {
            refreshTokenService.saveRefreshToken(user.getUserId(), newRefreshToken);
        }
        return newRefreshToken;
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();

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
}
