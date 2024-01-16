package org.example.weneedbe.global.config.oauth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String accessToken;
    private String tokenType;


    public LoginResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}
