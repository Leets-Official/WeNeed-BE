package org.example.weneedbe.global.config.oauth;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final String TOKEN_URI = "https://oauth2.googleapis.com/token";

    @Value("${spring.security.oauth2.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.google.redirect-uri}")
    private String LOGIN_REDIRECT_URI;

    @Autowired
    private final UserRepository userRepository;

    public ResponseEntity<String> getAccessToken(String accessCode) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", accessCode);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("redirect_uri", LOGIN_REDIRECT_URI);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(TOKEN_URI, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }
}
