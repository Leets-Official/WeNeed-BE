package org.example.weneedbe.domain.oauth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.example.weneedbe.domain.oauth.dto.OAuthToken;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.jwt.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class OAuthService {
    private final UserRepository userRepository;

    private final static String TOKEN_BASE_URI = "https://oauth2.googleapis.com/token";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    public OAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getGoogleToken(String code) throws GeneralSecurityException, IOException {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("redirect_uri", REDIRECT_URI);
        params.put("grant_type", "authorization_code");

        ResponseEntity<OAuthToken> responseEntity = restTemplate.postForEntity(TOKEN_BASE_URI, params, OAuthToken.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            throw new IllegalArgumentException();
        }

        String idToken = responseEntity.getBody().getId_token();
        return getUser(idToken);
    }


    public User getUser(String idToken) throws GeneralSecurityException, IOException {

        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);
        if (idToken == null) {
            throw new InvalidTokenException();
        }

        Payload payload = googleIdToken.getPayload();

        Optional<User> byEmail = userRepository.findByEmail(payload.getEmail());

        if (byEmail.isPresent()) {
            return byEmail.get();
        }

        User user = User.builder()
                .email(payload.getEmail())
                .hasRegistered(false)
                .build();

        return userRepository.save(user);
    }
}