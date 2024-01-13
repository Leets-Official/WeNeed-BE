package org.example.weneedbe.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.global.config.oauth.OAuth2SuccessHandler;
import org.example.weneedbe.global.config.oauth.OAuth2UserCustomService;
import org.example.weneedbe.global.config.oauth.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/")
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("code/google")
    public ResponseEntity<String> successGoogleLogin(@RequestParam("code") String accessCode) {
        return oAuthService.getAccessToken(accessCode);
    }
}
