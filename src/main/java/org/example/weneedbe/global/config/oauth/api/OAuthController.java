package org.example.weneedbe.global.config.oauth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.global.config.jwt.TokenProvider;
import org.example.weneedbe.global.config.oauth.OAuthService;
import org.example.weneedbe.global.config.oauth.dto.LoginResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {
    private final TokenProvider tokenProvider;
    private final OAuthService oAuthService;

    @GetMapping("/api/auth/callback/google")
    public LoginResponse get(@RequestParam("code") String code) throws GeneralSecurityException, IOException {
        User user = oAuthService.getGoogleToken(code);
        String accessToken = this.tokenProvider.generateAccessToken(user);
        String refreshToken = this.tokenProvider.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }
}
