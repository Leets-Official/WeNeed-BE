package org.example.weneedbe.global.config.oauth.api;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.user.service.UserService;
import org.example.weneedbe.global.config.oauth.dto.LoginRequest;
import org.example.weneedbe.global.config.oauth.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {
    private final UserService userService;

    @PostMapping(value = "/user/login")
    public ResponseEntity<LoginResponse> oauthLogin(
            @RequestBody @Valid LoginRequest loginRequest) {

        String code = loginRequest.getAuthorizationCode();
        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        String accessToken = userService.googleOauthLogin(decode);
        return ResponseEntity.created(URI.create("/user/login"))
                .body(new LoginResponse(accessToken, "bearer"));
    }
}
