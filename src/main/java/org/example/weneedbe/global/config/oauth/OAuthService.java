package org.example.weneedbe.global.config.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.example.weneedbe.global.config.oauth.dto.OAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class OAuthService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public OAuthService(RestTemplate restTemplate) {
        this.objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.restTemplate = restTemplate;
    }

    private final static String TOKEN_BASE_URI = "https://oauth2.googleapis.com/token";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    public ResponseEntity<String> createPostRequest(String code) {

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");
        params.add("access_type", "offline");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(TOKEN_BASE_URI, params, String.class);

        return restTemplate.exchange(TOKEN_BASE_URI, HttpMethod.POST, responseEntity, String.class);
    }

    public OAuthToken getAccessToken(ResponseEntity<String> response) {
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oAuthToken;
    }

    public ResponseEntity<String> createGetRequest(OAuthToken oAuthToken) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public String getUserEmail(ResponseEntity<String> userInfoResponse) {
        String email = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(userInfoResponse.getBody());
            if (jsonNode.has("email")) {
                email = jsonNode.get("email").asText();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return email;
    }

}